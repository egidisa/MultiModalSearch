package multiModal;

public class IndexEntry {
    public String id;
    public String tags;
    public String imgDesc;
    public String classLabel;

    public IndexEntry(String id, String tags, String imgDesc, String classLabel) {
    	this.id = id;
    	this.tags = tags;
    	this.imgDesc = imgDesc;
        this.classLabel = classLabel;
    }

}
