package at.co.ait.domain.services;

import java.io.File;
import java.io.IOException;

import org.ontoware.rdf2go.RDF2Go;
import org.ontoware.rdf2go.exception.ModelException;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.ModelSet;
import org.ontoware.rdf2go.model.Syntax;
import org.ontoware.rdf2go.model.node.URI;
import org.semanticdesktop.aperture.accessor.DataObject;
import org.semanticdesktop.aperture.accessor.impl.DefaultDataAccessorRegistry;
import org.semanticdesktop.aperture.crawler.Crawler;
import org.semanticdesktop.aperture.crawler.ExitCode;
import org.semanticdesktop.aperture.crawler.base.CrawlerHandlerBase;
import org.semanticdesktop.aperture.crawler.filesystem.FileSystemCrawler;
import org.semanticdesktop.aperture.datasource.filesystem.FileSystemDataSource;
import org.semanticdesktop.aperture.extractor.ExtractorException;
import org.semanticdesktop.aperture.mime.identifier.magic.MagicMimeTypeIdentifier;
import org.semanticdesktop.aperture.rdf.RDFContainer;
import org.semanticdesktop.aperture.rdf.impl.RDFContainerFactoryImpl;
import org.semanticdesktop.aperture.rdf.impl.RDFContainerImpl;
import org.semanticdesktop.aperture.subcrawler.SubCrawlerException;

import at.co.ait.domain.oais.InformationPackageObject;

public class ApertureService {

	private Boolean suppressParentChildLinks = Boolean.FALSE;

	public InformationPackageObject process(InformationPackageObject obj) throws Exception {		
        // start crawling and exit afterwards
        doCrawling(obj.getSubmittedFile());     
        return obj;
	}
	
    public void doCrawling(File rootFile) throws ModelException {
        // create a data source configuration
        RDFContainerFactoryImpl factory = new RDFContainerFactoryImpl();
        RDFContainer configuration = factory.newInstance("source:testsource");

        // create the data source
        FileSystemDataSource source = new FileSystemDataSource();
        source.setConfiguration(configuration);
        source.setRootFolder(rootFile.getAbsolutePath());
        source.setSuppressParentChildLinks(suppressParentChildLinks);

        // setup a crawler that can handle this type of DataSource
        FileSystemCrawler crawler = new FileSystemCrawler();
        crawler.setDataSource(source);
        crawler.setDataAccessorRegistry(new DefaultDataAccessorRegistry());
        crawler.setCrawlerHandler(new TutorialCrawlerHandler());

        // start crawling
        crawler.crawl();
        System.out.println(crawler.getCrawlReport().getNewCount());
    }
    
    private class TutorialCrawlerHandler extends CrawlerHandlerBase {

		// our 'persistent' modelSet
        private ModelSet modelSet;

        public TutorialCrawlerHandler() throws ModelException {
        	super(new MagicMimeTypeIdentifier(),null,null);
            modelSet = RDF2Go.getModelFactory().createModelSet();
            modelSet.open();            
        }

        public void crawlStopped(Crawler crawler, ExitCode exitCode) {
            try {
                modelSet.writeTo(System.out, Syntax.RdfXml);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }

            modelSet.close();
        }

        public RDFContainer getRDFContainer(URI uri) {
            // we create a new in-memory temporary model for each data source
            Model model = RDF2Go.getModelFactory().createModel(uri);
            model.open();
            return new RDFContainerImpl(model, uri);
        }

        public void objectNew(Crawler crawler, DataObject object) {
        	try {
				processBinary(crawler, object);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExtractorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SubCrawlerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
            // then we add this information to our persistent model
            modelSet.addModel(object.getMetadata().getModel());
            // don't forget to dipose of the DataObject
            object.dispose();
        }

        public void objectChanged(Crawler crawler, DataObject object) {
            // first we remove old information about the data object
            modelSet.removeModel(object.getID());
            // then we try to extract metadata and fulltext from the file
            try {
				processBinary(crawler, object);				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExtractorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SubCrawlerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            // an then we add the information from the temporary model to our
            // 'persistent' model
            modelSet.addModel(object.getMetadata().getModel());
            // don't forget to dispose of the DataObject
            object.dispose();
        }

        public void objectRemoved(Crawler crawler, URI uri) {
            modelSet.removeModel(uri);
        }
    }

}
