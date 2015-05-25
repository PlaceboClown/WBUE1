package data;

import at.ac.tuwien.big.we.dbpedia.api.DBPediaService;
import at.ac.tuwien.big.we.dbpedia.api.SelectQueryBuilder;
import at.ac.tuwien.big.we.dbpedia.vocabulary.DBPProp;
import at.ac.tuwien.big.we.dbpedia.vocabulary.DBPedia;

import at.ac.tuwien.big.we.dbpedia.vocabulary.DBPediaOWL;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.DCTerms;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import models.Answer;
import models.Category;
import models.Question;
import play.Logger;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * @author Lukas
 * @version 27.05.2015.
 */
public class DBpediaDataInserter {

    public static Category insertCategoryMovie() throws Exception{
        Category category = new Category();
        category.setNameDE("Filme");
        category.setNameEN("Movies");

        try {
            category.addQuestion(marvelMovies());
            category.addQuestion(moviesSpielbergAfter2000());
            category.addQuestion(directorAndActor());
            category.addQuestion(moviesFromBradPitt());
            category.addQuestion(categoryAmericaFilms());

            return category;
        } catch (Exception e) {
            Logger.error("error reading DBpediaData");
            throw new Exception();
        }
    }

    private static Question marvelMovies() throws Exception {
        if(!DBPediaService.isAvailable()) {
            Logger.info("DBpedia is currently not available.");
            throw new Exception();
        }
        Resource marvelStudios = DBPediaService.loadStatements(DBPedia.createResource("Marvel_Studios"));

        SelectQueryBuilder movieQuery = DBPediaService.createQueryBuilder()
                .setLimit(3)
                .addWhereClause(RDF.type, DBPediaOWL.Film)
                .addPredicateExistsClause(FOAF.name)
                .addWhereClause(DBPProp.createProperty("studio"), marvelStudios)
                .addFilterClause(RDFS.label, Locale.GERMAN)
                .addFilterClause(RDFS.label, Locale.ENGLISH);

        Model model = DBPediaService.loadStatements(movieQuery.toQueryString());

        List<String> moviesMarvelGerman = DBPediaService.getResourceNames(model, Locale.GERMAN);
        List<String> moviesMarvelEnglish = DBPediaService.getResourceNames(model, Locale.ENGLISH);

        movieQuery.removeWhereClause(DBPProp.createProperty("studio"), marvelStudios);
        movieQuery.addMinusClause(DBPProp.createProperty("studio"), marvelStudios);
        movieQuery.setOffset(50);

        model = DBPediaService.loadStatements(movieQuery.toQueryString());

        List<String> noMoviesMarvelGerman = DBPediaService.getResourceNames(model, Locale.GERMAN);
        List<String> noMoviesMarvelEnglish = DBPediaService.getResourceNames(model, Locale.ENGLISH);

        Question question = new Question();
        question.setTextDE("Welche Filme wurden von Marvel produziert?");
        question.setTextEN("Which films were produced by Marvel?");
        question.setValue(10);
        for (int i = 0; i < moviesMarvelGerman.size(); i++) {
            Answer a = new Answer();
            a.setTextDE(moviesMarvelGerman.get(i));
            a.setTextEN(moviesMarvelEnglish.get(i));
            question.addRightAnswer(a);
        }

        for (int i = 0; i < noMoviesMarvelGerman.size(); i++) {
            Answer a = new Answer();
            a.setTextDE(noMoviesMarvelGerman.get(i));
            a.setTextEN(noMoviesMarvelEnglish.get(i));
            question.addWrongAnswer(a);

        }
        Logger.info(question.toString());
        return question;
    }

    public static Question moviesSpielbergAfter2000() throws Exception {
        if(!DBPediaService.isAvailable()) {
            Logger.info("DBpedia is currently not available.");
            throw new Exception();
        }
        Resource spielberg = DBPediaService.loadStatements(DBPedia.createResource("Steven_Spielberg"));
        Calendar calendar = new GregorianCalendar(2000,0,1);

        SelectQueryBuilder movieQueryAfter2000 = DBPediaService.createQueryBuilder()
                .setLimit(3)
                .addWhereClause(RDF.type, DBPediaOWL.Film)
                .addPredicateExistsClause(FOAF.name)
                .addWhereClause(DBPediaOWL.director, spielberg)
                .addFilterClause(DBPProp.createProperty("released"), calendar, SelectQueryBuilder.MatchOperation.GREATER_OR_EQUAL)
                .addFilterClause(RDFS.label, Locale.GERMAN)
                .addFilterClause(RDFS.label, Locale.ENGLISH);

        Model model = DBPediaService.loadStatements(movieQueryAfter2000.toQueryString());

        List<String> moviesGermanAfter = DBPediaService.getResourceNames(model, Locale.GERMAN);
        List<String> moviesEnglishAfter = DBPediaService.getResourceNames(model, Locale.ENGLISH);

        SelectQueryBuilder movieQueryBefore2000 = DBPediaService.createQueryBuilder()
                .setLimit(3)
                .addWhereClause(RDF.type, DBPediaOWL.Film)
                .addPredicateExistsClause(FOAF.name)
                .addWhereClause(DBPediaOWL.director, spielberg)
                .addFilterClause(DBPProp.createProperty("released"), calendar, SelectQueryBuilder.MatchOperation.LESS)
                .addFilterClause(RDFS.label, Locale.GERMAN)
                .addFilterClause(RDFS.label, Locale.ENGLISH);

        model = DBPediaService.loadStatements(movieQueryBefore2000.toQueryString());

        List<String> moviesGermanBefore = DBPediaService.getResourceNames(model, Locale.GERMAN);
        List<String> moviesEnglishBefore = DBPediaService.getResourceNames(model, Locale.ENGLISH);

        Question question = new Question();
        question.setTextDE("Welche Filme wurden von Steven Spielberg nach 2000 produziert?");
        question.setTextEN("Which films were produced by Steven Spielberg after 2000?");
        question.setValue(20);
        for (int i = 0; i < moviesGermanAfter.size(); i++) {
            Answer a = new Answer();
            a.setTextDE(moviesGermanAfter.get(i));
            a.setTextEN(moviesEnglishAfter.get(i));
            question.addRightAnswer(a);
        }

        for (int i = 0; i < moviesGermanBefore.size(); i++) {
            Answer a = new Answer();
            a.setTextDE(moviesGermanBefore.get(i));
            a.setTextEN(moviesEnglishBefore.get(i));
            question.addWrongAnswer(a);
        }
        Logger.info(question.toString());
        return question;
    }

    public static Question directorAndActor() throws Exception {
        if(!DBPediaService.isAvailable()) {
            Logger.info("DBpedia is currently not available.");
            throw new Exception();
        }
        Resource director = DBPediaService.loadStatements(DBPedia.createResource("Gary_Ross"));
        Resource actor = DBPediaService.loadStatements(DBPedia.createResource("Liam_Hemsworth"));

        SelectQueryBuilder movieQuery = DBPediaService.createQueryBuilder()
                .setLimit(5) // at most five statements
                .addWhereClause(RDF.type, DBPediaOWL.Film)
                .addPredicateExistsClause(FOAF.name)
                .addWhereClause(DBPediaOWL.director, director)
                .addWhereClause(DBPediaOWL.starring, actor)
                .addFilterClause(RDFS.label, Locale.GERMAN)
                .addFilterClause(RDFS.label, Locale.ENGLISH);

        Model movies = DBPediaService.loadStatements(movieQuery.toQueryString());

        List<String> englishMovieNames = DBPediaService.getResourceNames(movies, Locale.ENGLISH);
        List<String> germanMovieNames = DBPediaService.getResourceNames(movies, Locale.GERMAN);

        movieQuery.removeWhereClause(DBPediaOWL.starring, actor);
        movieQuery.addMinusClause(DBPediaOWL.starring, actor);

        Model noMovies = DBPediaService.loadStatements(movieQuery.toQueryString());
        List<String> noMoviesEnglish = DBPediaService.getResourceNames(noMovies, Locale.ENGLISH);
        List<String> noMoviesGerman = DBPediaService.getResourceNames(noMovies, Locale.GERMAN);

        Question question = new Question();
        question.setTextDE("In welchen Filmen spielte Liam Hemsworth mit und Gary Ross Regie?");
        question.setTextEN("In which movies played Liam Hemsworth and directed by Gary Ross?");
        question.setValue(30);
        for (int i = 0; i < germanMovieNames.size(); i++) {
            Answer a = new Answer();
            a.setTextDE(germanMovieNames.get(i));
            a.setTextEN(englishMovieNames.get(i));
            question.addRightAnswer(a);
        }

        for (int i = 0; i < noMoviesEnglish.size(); i++) {
           Answer a = new Answer();
            a.setTextDE(noMoviesGerman.get(i));
            a.setTextEN(noMoviesEnglish.get(i));
            question.addRightAnswer(a);
        }
        Logger.info(question.toString());
        return question;
    }

    public static Question moviesFromBradPitt() throws Exception {
        if(!DBPediaService.isAvailable()) {
            Logger.info("DBpedia is currently not available.");
            throw new Exception();
        }
        Resource acctor = DBPediaService.loadStatements(DBPedia.createResource("Brad_Pitt"));

        SelectQueryBuilder query = DBPediaService.createQueryBuilder()
                .setLimit(2)
                .addWhereClause(RDF.type, DBPediaOWL.Film)
                .addPredicateExistsClause(FOAF.name)
                .addWhereClause(DBPediaOWL.starring, acctor)
                .addFilterClause(DBPediaOWL.budget, 5000000, SelectQueryBuilder.MatchOperation.GREATER_OR_EQUAL)
                .addFilterClause(RDFS.label, Locale.GERMAN)
                .addFilterClause(RDFS.label, Locale.ENGLISH);

        Model model = DBPediaService.loadStatements(query.toQueryString());

        List<String> moviesGerman = DBPediaService.getResourceNames(model, Locale.GERMAN);
        List<String> moviesEnglish = DBPediaService.getResourceNames(model, Locale.ENGLISH);

        query.removeWhereClause(DBPediaOWL.starring, acctor);
        query.addMinusClause(DBPediaOWL.starring, acctor);
        query.setLimit(4);

        model = DBPediaService.loadStatements(query.toQueryString());

        List<String> noMoviesGerman = DBPediaService.getResourceNames(model, Locale.GERMAN);
        List<String> noMoviesEnglish = DBPediaService.getResourceNames(model, Locale.ENGLISH);

        Question question = new Question();
        question.setTextDE("In welchen Filmen spielte Brad Pitt mit, welche ein Budget von über 5 Millionen Doller hatten?");
        question.setTextEN("In what movies Brad Pitt played, which had a budget of over 5 million doller?");
        question.setValue(40);
        for (int i = 0; i < moviesGerman.size(); i++) {
            Answer a = new Answer();
            a.setTextDE(moviesGerman.get(i));
            a.setTextEN(moviesEnglish.get(i));
            question.addRightAnswer(a);
        }

        for (int i = 0; i < noMoviesGerman.size(); i++) {
            Answer a = new Answer();
            a.setTextDE(noMoviesGerman.get(i));
            a.setTextEN(noMoviesEnglish.get(i));

            question.addWrongAnswer(a);
        }
        Logger.info(question.toString());
        return question;
    }

    public static Question categoryAmericaFilms() throws Exception {
        if(!DBPediaService.isAvailable()) {
            Logger.info("DBpedia is currently not available.");
            throw new Exception();
        }

        Resource america = DBPediaService.loadStatements(DBPedia.createResource("Category:American_films"));

        SelectQueryBuilder query = DBPediaService.createQueryBuilder()
                .setLimit(2)
                .addWhereClause(RDF.type, DBPediaOWL.Film)
                .addPredicateExistsClause(FOAF.name)
                .addWhereClause(DCTerms.subject, america)
                .addFilterClause(RDFS.label, Locale.GERMAN)
                .addFilterClause(RDFS.label, Locale.ENGLISH);

        Model model = DBPediaService.loadStatements(query.toQueryString());

        List<String> moviesGerman = DBPediaService.getResourceNames(model, Locale.GERMAN);
        List<String> moviesEnglish = DBPediaService.getResourceNames(model, Locale.ENGLISH);

        query.removeWhereClause(DCTerms.subject, america);
        query.addMinusClause(DCTerms.subject, america);
        query.setLimit(4);

        model = DBPediaService.loadStatements(query.toQueryString());

        List<String> noCategoryAmericaGerman = DBPediaService.getResourceNames(model, Locale.GERMAN);
        List<String> noCategoryAmericaEnglish = DBPediaService.getResourceNames(model, Locale.ENGLISH);

        Question question = new Question();
        question.setTextDE("Welche Filme sind Amerikanische?");
        question.setTextEN("What movies are American?");
        question.setValue(50);
        for (int i = 0; i < moviesGerman.size(); i++) {
            Answer a = new Answer();
            a.setTextDE(moviesGerman.get(i));
            a.setTextEN(moviesEnglish.get(i));
            question.addRightAnswer(a);
        }

        for (int i = 0; i < noCategoryAmericaGerman.size(); i++) {
            Answer a = new Answer();
            a.setTextDE(noCategoryAmericaGerman.get(i));
            a.setTextEN(noCategoryAmericaEnglish.get(i));

            question.addWrongAnswer(a);
        }
        Logger.info(question.toString());
        return question;
    }
}
