package multiModal;
/***
 * Simple class to handle the index entries retrieved from the index. Has two constructors, with and without score.
 * @author Sara
 *
 */
public class IndexEntry {
    private String id;
    private String tags;
    private String imgDesc;
    private String imgDesc6;
    private String classLabel;
    private String score;

	public IndexEntry(String id, String tags, String imgDesc, String imgDesc6, String classLabel) {
    	this.id = id;
    	this.tags = tags;
    	this.imgDesc = imgDesc;
    	this.imgDesc6 = imgDesc6;
        this.classLabel = classLabel;
    }
    
    public IndexEntry(String id, String tags, String imgDesc, String imgDesc6, String classLabel, String score) {
    	this.id = id;
    	this.tags = tags;
    	this.imgDesc = imgDesc;
    	this.imgDesc6 = imgDesc6;
        this.classLabel = classLabel;
        this.score = score;
    }
    
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getImgDesc() {
		return imgDesc;
	}

	public void setImgDesc(String imgDesc) {
		this.imgDesc = imgDesc;
	}

	public String getImgDesc6() {
		return imgDesc6;
	}

	public void setImgDesc6(String imgDesc6) {
		this.imgDesc6 = imgDesc6;
	}

	
	public String getClassLabel() {
		return classLabel;
	}

	public void setClassLabel(String classLabel) {
		this.classLabel = classLabel;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}
}
