import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import at.ac.tuwien.big.we.dbpedia.api.DBPediaService;
import at.ac.tuwien.big.we.dbpedia.vocabulary.DBPedia;
import data.DBpediaDataInserter;
import models.Category;
import models.JeopardyDAO;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.Play;
import play.db.jpa.JPA;
import play.libs.F.Function0;
import data.JSONDataInserter;

public class Global extends GlobalSettings {
	
	@play.db.jpa.Transactional
	public static void insertJSonData() throws IOException {
		String file = Play.application().configuration().getString("questions.filePath");		
		Logger.info("Data from: " + file);
		InputStream is = Play.application().resourceAsStream(file);
		List<Category> categories = JSONDataInserter.insertData(is);
		Logger.info(categories.size() + " categories from json file '" + file + "' inserted.");

	}

	@play.db.jpa.Transactional
	public static void insertDBpediaData() throws IOException {
		try {
			Category category = DBpediaDataInserter.insertCategoryMovie();
			JeopardyDAO.INSTANCE.persist(category);
			Logger.info("insert category movie");
		} catch (Exception e) {
			Logger.error("cannot create category movie");
		}
	}

	@play.db.jpa.Transactional
    public void onStart(Application app) {
       try {
    	   JPA.withTransaction(new Function0<Boolean>() {

			@Override
			public Boolean apply() throws Throwable {
				insertJSonData();
				insertDBpediaData();
				return true;
			}
			   
			});
       } catch (Throwable e) {
    	   e.printStackTrace();
       }
    }

    public void onStop(Application app) {
        Logger.info("Application shutdown...");
    }

}