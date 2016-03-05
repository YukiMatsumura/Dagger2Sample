package yuki.m.android.dagger2sample.domain;

public class GitHubRepository {
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("GitHubRepository{id:%s, name:%s}", id, name);
    }

}
