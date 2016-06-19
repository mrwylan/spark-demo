package wylan.ch;

import java.io.Serializable;
import java.time.LocalDate;

public class SparkDemoComment implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String comment;
	private LocalDate creationDate = LocalDate.now();
	
	public SparkDemoComment(String comment){
		this.comment = comment;
	}
	public SparkDemoComment(Integer id, String comment){
		this.id = id;
		this.comment = comment;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public LocalDate getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(LocalDate creationDate) {
		this.creationDate = creationDate;
	}
	
}
