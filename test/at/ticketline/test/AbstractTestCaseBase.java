package at.ticketline.test;

import java.io.File;

import org.apache.log4j.Logger;
import org.dbunit.AbstractDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.hsqldb.HsqldbConnection;

import at.ticketline.dao.EntityManagerUtil;

public class AbstractTestCaseBase extends AbstractDatabaseTester {
    
    private Logger log = Logger.getLogger(AbstractTestCaseBase.class);

    protected static final String PATH_TO_DS = "test" + File.separator + "at" + 
    			File.separator + "ticketline" + File.separator + "test" + 
    			File.separator + "dataset" + File.separator;
    
    public AbstractTestCaseBase () {
	TestInitializer.init();
	EntityManagerUtil.getCurrentEntityManager();
	TestUtil.clearTables();
    }
    
    @Override
    public IDatabaseConnection getConnection() throws Exception {
	return new HsqldbConnection(HSQLDBHandler.getInstance().getConnection(), "PUBLIC");
    }
    
    protected IDataSet getDataSet(String sDatasetName) {
	File fDataset = new File(PATH_TO_DS + sDatasetName);
	if (!fDataset.exists()) {
	    log.error("Specified Dataset does not exist!");
	}

	FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
	IDataSet ds = null;
	
	try {
	    ds = builder.build(fDataset);
	} catch (Exception e) {
	    log.error("Error while building Dataset");
	    e.printStackTrace();
	}
	return ds;
    }
}
