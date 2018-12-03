package fixed;

public class UpdateInfo {
    private String update_url;
    private int update_ver_code;
    private String update_content;
    private String update_ver_name;
    private String md5;
    private boolean ignore_able;
    private boolean forced;

    public boolean isForced() {
        return forced;
    }

    public void setForced(boolean forced) {
        this.forced = forced;
    }

    public boolean isIgnore_able() {
        return ignore_able;
    }

    public void setIgnore_able(boolean ignore_able) {
        this.ignore_able = ignore_able;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getUpdate_ver_name() {
        return update_ver_name;
    }

    public void setUpdate_ver_name(String update_ver_name) {
        this.update_ver_name = update_ver_name;
    }

    public String getUpdate_url() {
        return update_url;
    }

    public void setUpdate_url(String update_url) {
        this.update_url = update_url;
    }

    public int getUpdate_ver_code() {
        return update_ver_code;
    }

    public void setUpdate_ver_code(int update_ver_code) {
        this.update_ver_code = update_ver_code;
    }

    public String getUpdate_content() {
        return update_content;
    }

    public void setUpdate_content(String update_content) {
        this.update_content = update_content;
    }
}
