package wylan.ch;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.put;
import static spark.Spark.before;
import static spark.Spark.halt;
import static spark.Spark.staticFiles;

import java.util.logging.Logger;
/**
 * SparkDemo
 *
 */
public class SparkDemo 
{
    private static String comment;

	public static void main( String[] args )
    {
		staticFiles.location("/public");

    	port((int) Integer.valueOf(args[0]));
    	
		get("/sparkdemo", (req, res) -> "Welcome to SparkDemo!");
		
		get("/comments", (req, res) -> comment);
		
		put("/comment", (req, res) -> {comment = req.body(); Logger.getLogger(SparkDemo.class.getSimpleName()).info(comment); return "ok";});
		
		before("/", (request, response) -> {
		    halt(401, "Go Away!");
		});
		
		get("index.html", (req, res) -> {res.redirect("/sparkdemo");return null;});
		
		
		
    }
}
