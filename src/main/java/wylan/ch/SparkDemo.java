package wylan.ch;

import static spark.Spark.after;
import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.halt;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.staticFiles;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * SparkDemo
 *
 */
public class SparkDemo {
	private static String comment;

	private static List<String> snippets = new ArrayList<>();

	public static void main(String[] args) {

		staticFiles.location("/public");

		port((int) Integer.valueOf(args[0]));

		get("/sparkdemo", (req, res) -> "Welcome to SparkDemo!");

		get("/comments", (req, res) -> comment);

		put("/comment", (req, res) -> {
			comment = req.body();
			Logger.getLogger(SparkDemo.class.getSimpleName()).info(comment);
			return "ok";
		});

		before("/", (request, response) -> {
			halt(401, "Go Away!");
		});

		get("index.html", (req, res) -> {
			res.redirect("/sparkdemo");
			return null;
		});

		SparkDemoService.getInstance();

		post("/democomments",
				(req, res) -> SparkDemoService.getInstance().add(new SparkDemoComment(req.queryParams("comment"))),
				JsonUtil.json());
		get("/democomments", (req, res) -> SparkDemoService.getInstance().getAll(), JsonUtil.json());
		get("/democomments/:id", (req, res) -> {
			String id = req.params(":id");
			return SparkDemoService.getInstance().get(Integer.valueOf(id));
		}, JsonUtil.json());
		put("/democomments/:id",
				(req, res) -> SparkDemoService.getInstance()
						.update(new SparkDemoComment(Integer.valueOf(req.params("id")), req.queryParams("comment"))),
				JsonUtil.json());

		after((req, res) -> {
			res.type("application/json");
		});
	}
}
