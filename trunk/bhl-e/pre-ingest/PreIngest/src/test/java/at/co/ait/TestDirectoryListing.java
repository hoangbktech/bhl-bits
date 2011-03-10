package at.co.ait;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.co.ait.domain.DirectoryListing;

public class TestDirectoryListing {
	
	private static final Logger logger = LoggerFactory.getLogger(TestDirectoryListing.class);
	private DirectoryListing dirlist = new DirectoryListing();

	@Before
	public void setUp() throws Exception {		
		dirlist.setBasedir("C:\\ProjectData\\BHL-E-FTP\\bhle-csic");
	}

	@After
	public void tearDown() throws Exception {
	}


}
