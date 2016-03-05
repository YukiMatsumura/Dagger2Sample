package yuki.m.android.dagger2sample.store;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Setter;
import com.github.gfx.android.orma.annotation.Table;

@Table("github_repository")
public class GitHubRepositoryRecord {

    @PrimaryKey
    public final long id;

    @Column
    public final String name;

    @Setter
    public GitHubRepositoryRecord(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("RepositoryTable{id:%s, name:%s}", id, name);
    }
}
