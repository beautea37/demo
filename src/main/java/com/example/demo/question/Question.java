package com.example.demo.question;

import com.example.demo.answer.Answer;
import com.example.demo.user.SiteUser;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 200)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;

    @ManyToOne
    private SiteUser author;

    private LocalDateTime modifyDate;

    @ManyToMany
    Set<SiteUser> voter;

//    @Builder
//    public Question(String subject, String content, LocalDateTime createDate,
//                    List<Answer> answerList, SiteUser author, LocalDateTime modifyDate, Set<SiteUser> voter) {
//        this.subject = subject;
//        this.content = content;
//        this.createDate = createDate;
//        this.answerList = answerList;
//        this.author = author;
//        this.modifyDate = modifyDate;
//        this.voter = voter;
//    }

    @Builder(toBuilder = true)
    public Question(String subject, String content, LocalDateTime createDate,
                    List<Answer> answerList, SiteUser author, LocalDateTime modifyDate, Set<SiteUser> voter) {
        this.subject = subject;
        this.content = content;
        this.createDate = createDate;
        this.answerList = answerList;
        this.author = author;
        this.modifyDate = modifyDate;
        this.voter = voter;
    }
}