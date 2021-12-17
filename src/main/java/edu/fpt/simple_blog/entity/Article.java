package edu.fpt.simple_blog.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Article {

    public Article(long id) {
        this.id = id;
    }

    public Article(long id, Date postingDate, String title, String description, User author) {
        this.id = id;
        this.postingDate = postingDate;
        this.title = title;
        this.description = description;
        this.author = author;
    }

    public Article(Date postingDate, String title, String description, String content, User author) {
        this.postingDate = postingDate;
        this.title = title;
        this.description = description;
        this.content = content;
        this.author = author;
    }

    public Article(long id, Date postingDate, String title, String description, User author, Status status) {
        this.id = id;
        this.postingDate = postingDate;
        this.title = title;
        this.description = description;
        this.author = author;
        this.status = status;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private Date postingDate;

    @Column(length = 200)
    private String title;

    @Column(length = 600)
    private String description;

    @Column(length = 10_000)
    private String content;

    @ManyToOne
    @JoinColumn(name = "author_email", nullable = false)
    private User author;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "article", fetch = FetchType.EAGER)
    private List<Comment> comments;

    public enum Status {
        NEW, ACTIVE, DELETED
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", postingDate=" + postingDate +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", content='" + content + '\'' +
                ", author=" + author +
                ", status=" + status +
                ", comments=" + comments +
                '}';
    }
}
