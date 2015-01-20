package entity;
/**
 *
 * @author User
 */
public class Staff{
    private int staffID;
    private int maxProjectNum;
    private String name;
    private String role;
    private String email;
    private String status;
    private String memberId;
    private String token;

    public Staff(int staffID, int maxProjectNum, String name, String role, String email, String status) {
        this.staffID = staffID;
        this.maxProjectNum = maxProjectNum;
        this.name = name;
        this.role = role;
        this.email = email;
        this.status = status;
    }
    
    public Staff(int staffID, int maxProjectNum, String name, String role, String email, String status, 
    		String memberId, String token) {
        this.staffID = staffID;
        this.maxProjectNum = maxProjectNum;
        this.name = name;
        this.role = role;
        this.email = email;
        this.status = status;
        this.memberId = memberId;
        this.token = token;
    }
    
    public String getMemberId() {
    	if(memberId != null){
    		return memberId;
    	}else{
    		return "";
    	}
    }
    
	public void setMemberId(String membeId) {
		this.memberId = memberId;
	}

	public String getToken() {
		if(token != null){
			return token;
		}else{
			return "";
		}
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    public int getStaffID() {
        return staffID;
    }

    public int getMaxProjectNum() {
        return maxProjectNum;
    }

    public String getName() {
        return name;
    }

    public void setStaffID(int staffID) {
        this.staffID = staffID;
    }

    public void setMaxProjectNum(int maxProjectNum) {
        this.maxProjectNum = maxProjectNum;
    }

    public void setName(String name) {
        this.name = name;
    }

	@Override
	public String toString() {
		return "Staff [staffID=" + staffID + ", maxProjectNum=" + maxProjectNum
				+ ", name=" + name + ", role=" + role + ", email=" + email
				+ ", status=" + status + ", memberId=" + memberId
				+ ", token=" + token + "]";
	}
}