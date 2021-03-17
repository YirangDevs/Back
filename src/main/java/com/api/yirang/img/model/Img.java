package com.api.yirang.img.model;

import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.img.util.ImgType;
import com.sun.istack.Nullable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Getter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "img")
public class Img {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imgId;

    @Column(nullable = true)
    private String kakaoImgUrl;

    @Column(nullable = true)
    private String customImgUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ImgType imgType;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * 초기값은 모든 imgURL은 null 입니다.
     * kakaoType을 default 값으로 가집니다.
     */
    @Builder
    public Img(User user) {
        this.kakaoImgUrl = null;
        this.customImgUrl = null;
        this.imgType = ImgType.IMG_TYPE_KAKAO;
        this.user = user;
    }
}
