package entity;
import java.util.*;
/**
 *
 * @author User
 */
public class Subproject{
    private String projectId;
    private int subprojectId;
    private String subprojectName;
    private String projectDescription;
    private Date startDate;
    private Date endDate;
	private String status;
	private String subprojectType;
	private Date actualStartDate;
    private Date actualEndDate;
    private String subprojectCardId;
	
	public Subproject(String projectId, int subprojectId,String subprojectName, String projectDescription,
			Date startDate, Date endDate, String subprojectType,
			String status) {
		this.projectId = projectId;
		this.subprojectName = subprojectName;
		this.subprojectId = subprojectId;
		this.projectDescription = projectDescription;
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
		this.subprojectType = subprojectType;
	}
	
	public Subproject(String projectId, int subprojectId,String subprojectName, String projectDescription,
			Date startDate, Date endDate, String subprojectType,
			String status,Date actualStartDate, Date actualEndDate) {
		this.projectId = projectId;
		this.subprojectName = subprojectName;
		this.subprojectId = subprojectId;
		this.projectDescription = projectDescription;
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
		this.subprojectType = subprojectType;
		this.actualStartDate = actualStartDate;
		this.actualEndDate = actualEndDate;
	}
	
	public Subproject(String projectId, int subprojectId,String subprojectName, String projectDescription,
			Date startDate, Date endDate, String subprojectType,
			String status,Date actualStartDate, Date actualEndDate, String subprojectCardId) {
		this.projectId = projectId;
		this.subprojectName = subprojectName;
		this.subprojectId = subprojectId;
		this.projectDescription = projectDescription;
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
		this.subprojectType = subprojectType;
		this.actualStartDate = actualStartDate;
		this.actualEndDate = actualEndDate;
		this.subprojectCardId = subprojectCardId;
	}
	
	public int getSubprojectId() {
        return subprojectId;
    }
	
	public String getSubprojectName() {
		return subprojectName;
	}

	public String getProjectId() {
        return projectId;
    }
	
	public String getSubprojectType() {
        return subprojectType;
    }
	
	public String getStatus(){
		return status;
	}

    public String getSubprojectDescription() {
        return projectDescription;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }
    
    public Date getActualStartDate() {
        return actualStartDate;
    }

    public Date getActualEndDate() {
        return actualEndDate;
    }
    
    public String getSubprojectCardId(){
    	return subprojectCardId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date projectEndDate) {
        this.endDate = projectEndDate;
    }
	
	public void setStatus(String status){
		this.status = status;
	}
	
	public void setSubprojectCardId(String subprojectCardId){
		this.subprojectCardId = subprojectCardId;
	}
	
	public void setSubprojectType(String subprojectType) {
        this.subprojectType = subprojectType;
    }
}    