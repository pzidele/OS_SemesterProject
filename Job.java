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
		String [] stringArray = str.split(" ");
		this.id = stringArray[0];
		this.type = stringArray[1];
		this.clientID = stringArray[2];
		this.status = stringArray[3];
	}
//	public String [] splitString(String str){
//		String [] newString = str.split(" ");
//		return newString;
//	}

	//public Job(String str) {
//		String type = str.substring(str.length()-1).toUpperCase();
//		String id = str.substring(0,str.length()-1).toUpperCase();

	// call other constructor



	//this(str.substring(0,str.length()-1).toUpperCase(),str.substring(str.length()-1).toUpperCase(), "client1", "stat" );

	//}





	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}


	@Override
	public String toString() {
		return "Job{" +
				"id='" + id + '\'' +
				", type='" + type + '\'' +
				", status='" + status + '\'' +
				", clientID='" + clientID + '\'' +
				'}';
	}
}
