package entity;
import java.util.*;
/**
 *
 * @author User
 */
public class Project{
    private String projectId;
    private String projectDescription;
    private int projectManagerId;
    private Date projectStartDate;
    private Date projectEndDate;
    private Date actualStartDate;
    private Date actualEndDate;
    private Date warrantyStartDate;
    private Date warrantyEndDate;
    private String client;
	private String status;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String boardId;
	private int subProjectNumber;
	
	public Project(String projectId, String projectDescription,
			int projectManagerId, Date projectStartDate, Date projectEndDate,
			Date warrantyStartDate, Date warrantyEndDate, String client,
			String Status, String lastModifiedBy, Date lastModifiedDate) {
		this.projectId = projectId;
		this.projectDescription = projectDescription;
		this.projectManagerId = projectManagerId;
		this.projectStartDate = projectStartDate;
		this.projectEndDate = projectEndDate;
		this.warrantyStartDate = warrantyStartDate;
		this.warrantyEndDate = warrantyEndDate;
		this.client = client;
		this.status = status;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
	} 
	
    public Project(String projectId, String projectDescription,
			int projectManagerId, Date projectStartDate, Date projectEndDate,
			Date warrantyStartDate, Date warrantyEndDate, String client,
			String status, String lastModifiedBy, Date lastModifiedDate, String boardId) {
		this.projectId = projectId;
		this.projectDescription = projectDescription;
		this.projectManagerId = projectManagerId;
		this.projectStartDate = projectStartDate;
		this.projectEndDate = projectEndDate;
		this.warrantyStartDate = warrantyStartDate;
		this.warrantyEndDate = warrantyEndDate;
		this.client = client;
		this.status = status;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.boardId = boardId;
	}
    
    public Project(String projectId, String projectDescription,
			int projectManagerId, Date projectStartDate, Date projectEndDate, Date actualStartDate,
			Date actualEndDate, Date warrantyStartDate, Date warrantyEndDate, String client,
			String status, String lastModifiedBy, Date lastModifiedDate, String boardId, int subProjectNumber) {
		this.projectId = projectId;
		this.projectDescription = projectDescription;
		this.projectManagerId = projectManagerId;
		this.projectStartDate = projectStartDate;
		this.projectEndDate = projectEndDate;
		this.actualStartDate = actualStartDate;
		this.actualEndDate = actualEndDate;
		this.warrantyStartDate = warrantyStartDate;
		this.warrantyEndDate = warrantyEndDate;
		this.client = client;
		this.status = status;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.boardId = boardId;
		this.subProjectNumber = subProjectNumber;
	}
    
	public Date getActualStartDate() {
		return actualStartDate;
	}

	public void setActualStartDate(Date actualStartDate) {
		this.actualStartDate = actualStartDate;
	}

	public Date getActualEndDate() {
		return actualEndDate;
	}

	public void setActualEndDate(Date actualEndDate) {
		this.actualEndDate = actualEndDate;
	}

	public int getSubProjectNumber() {
		return subProjectNumber;
	}

	public void setSubProjectNumber(int subProjectNumber) {
		this.subProjectNumber = subProjectNumber;
	}

	public String getBoardId() {
		return boardId;
	}

	public void setBoardId(String boardId) {
		this.boardId = boardId;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getProjectId() {
        return projectId;
    }
	
	public String getStatus(){
		return status;
	}
	
	public int getProjectManagerId(){
		return projectManagerId;
	}

    public String getProjectDescription() {
        return projectDescription;
    }

    public Date getProjectStartDate() {
        return projectStartDate;
    }

    public Date getProjectEndDate() {
        return projectEndDate;
    }

    public Date getWarrantyStartDate() {
        return warrantyStartDate;
    }

    public Date getWarrantyEndDate() {
        return warrantyEndDate;
    }

    public String getClient() {
        return client;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public void setProjectStartDate(Date projectStartDate) {
        this.projectStartDate = projectStartDate;
    }

    public void setProjectEndDate(Date projectEndDate) {
        this.projectEndDate = projectEndDate;
    }

    public void setWarrantyStartDate(Date warrantyStartDate) {
        this.warrantyStartDate = warrantyStartDate;
    }

    public void setWarrantyEndDate(Date warrantyEndDate) {
        this.warrantyEndDate = warrantyEndDate;
    }
    
    public void setProjectManagerId(int managerId){
    	projectManagerId = managerId;
    }
    
    public void setClient(String client) {
        this.client = client;
    }
	
	public void setStatus(String status){
		this.status = status;
	}

	@Override
	public String toString() {
		return "Project [projectId=" + projectId + ", projectDescription="
				+ projectDescription + ", projectManagerId=" + projectManagerId
				+ ", projectStartDate=" + projectStartDate
				+ ", projectEndDate=" + projectEndDate + ", warrantyStartDate="
				+ warrantyStartDate + ", warrantyEndDate=" + warrantyEndDate
				+ ", client=" + client + ", Status=" + status
				+ ", lastModifiedBy=" + lastModifiedBy + ", lastModifiedDate="
				+ lastModifiedDate + ", boardId=" + boardId + "]";
	}
}    