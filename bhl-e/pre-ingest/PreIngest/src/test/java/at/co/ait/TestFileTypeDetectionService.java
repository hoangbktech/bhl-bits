package at.co.ait;


import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.co.ait.domain.services.DigitalObjectTypeExtractor;

public class TestFileTypeDetectionService {
	
	private static final Logger logger = LoggerFactory.getLogger(TestDirectoryListing.class);
	private DigitalObjectTypeExtractor service = new DigitalObjectTypeExtractor();
	
	@Before
	public void setUp() throws Exception {		
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testIdentifyFile() {
		logger.info("Testing identify");
		assertEquals("image/tiff",service.detectedMimeType(new File("C:\\ProjectData\\BHL-E-FTP\\bhle-mnhn\\PR 260 (Museum)\\ANNALES MUSEUM\\011802.DIR\\000001.TIF")));
		assertEquals("application/xml",service.detectedMimeType(new File("C:\\ProjectData\\BHL-E-FTP\\bhle-mnhn\\PR 260 (Museum)\\ANNALES MUSEUM\\011802.DIR\\TDM\\011802.xml")));
	}

}
