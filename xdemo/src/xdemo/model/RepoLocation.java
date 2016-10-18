package xdemo.model;

public class RepoLocation {
	public static final RepoLocation REPO_LOCATION = new RepoLocation();
	
	private String loc = "file:////C:/Repo";

	public String getLoc() {
		return loc;
	}

	public void setLoc(String loc) {
		this.loc = loc;
	}
	
}
