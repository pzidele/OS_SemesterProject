package project;

public class Job {

	private String id;
	private String type;
	private String status;
	private String sentToSlave;
	private String clientID;

	public void setSentToSlave(String sentToSlave) {
		this.sentToSlave = sentToSlave;
	}

	public String getSentToSlave() {
		return sentToSlave;
	}

	public String getClientID() {
		return clientID;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Job(String str) {
		String[] stringArray = str.split(" ");
		this.id = stringArray[0];
		this.type = stringArray[1];
		this.clientID = stringArray[2];
		this.status = stringArray[3];

	}

	// public Job(String id, String type, String clientID, String status, String
	// sentToSlave){
	public Job(String str, String sentToSlave) {
		String[] stringArray = str.split(" ");
		this.id = stringArray[0];
		this.type = stringArray[1];
		this.clientID = stringArray[2];
		this.status = stringArray[3];
		this.sentToSlave = sentToSlave;
	}

	public String getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Job{" + "id='" + id + '\'' + ", type='" + type + '\'' + ", status='" + status + '\'' + ", clientID='"
				+ clientID + '\'' + ", slave sent to=' " + sentToSlave + '\'' + '}';
	}
}
