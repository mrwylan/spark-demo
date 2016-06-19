package wylan.ch;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SparkDemoService {
	
	private static SparkDemoService serviceInstance;
	
	private Map<Integer, SparkDemoComment> comments= new HashMap<>();
	
	private SparkDemoService(){}
	
	public static SparkDemoService getInstance(){
		if(serviceInstance == null){
			serviceInstance = new SparkDemoService();
		}
		return serviceInstance;
	}
	
	public SparkDemoComment add(SparkDemoComment comment){
		Optional<Integer> maxIndex = comments.keySet().stream().reduce(Integer::max);
		comment.setId(maxIndex.orElse(1));
		return comments.put(comment.getId(), comment);
	}
	
	public Collection<SparkDemoComment> getAll(){
		return comments.values();
	}
	
	public SparkDemoComment update(SparkDemoComment comment){
		return comments.put(comment.getId(), comment);
	}
	
	public SparkDemoComment get(Integer id){
		return comments.get(id);
	}
	
	

}
