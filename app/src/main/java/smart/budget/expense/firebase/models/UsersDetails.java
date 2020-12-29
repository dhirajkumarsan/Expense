package smart.budget.expense.firebase.models;

public class UsersDetails {
    String uid,emi;

    public UsersDetails(String uid, String emi) {
        this.uid = uid;
        this.emi = emi;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmi() {
        return emi;
    }

    public void setEmi(String emi) {
        this.emi = emi;
    }
}
