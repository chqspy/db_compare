package db.domain;

/**
 * @author caiww
 * @date 2019-02-25 9:28:34
 */
public class DataBaseDO {
    //连接地址
    private String dbUrl;
    //连接登录用户名
    private String userName;
    //连接登录密码
    private String passWord;

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}
