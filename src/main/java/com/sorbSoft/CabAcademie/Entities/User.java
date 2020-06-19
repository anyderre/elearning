package com.sorbSoft.CabAcademie.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sorbSoft.CabAcademie.Entities.Enums.OrganizationType;
import com.sorbSoft.CabAcademie.Entities.Enums.SubscriptionPlanLevel;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * Created by anyderre on 11/08/17.
 */
@Data
@Entity
@Table(	name = "user", schema = "public",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
@Where(clause = "deleted=false")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotEmpty(message = "First name is required")
    private String firstName;

    @NotEmpty(message = "Last name is required")
    private String lastName;

    @NotEmpty(message = "Email is required")
    @Email(message = "Email is invalid")
    @Column(unique=true)
    private String email;

    @NotNull
    @NotEmpty(message = "UserName is required")
    @Size(min = 4, max = 30)
    private String username;

    @Column(name="enabled", columnDefinition = "int default 1")
    private int enable;

    @NotNull(message="Password invalid")
    @NotEmpty(message = "Password is required")
    @Size(max=60)
    @JsonIgnore
    private String password;

    @NotNull
    @Size(max = 2147483647)
    private String photoURL;

    @NotNull
    @Size(max = 2147483647)
    private String bio;


    private String country;

    @NotNull
    private String workspaceName;

    @ManyToOne(optional = false)
    @JoinColumn(name = "rol_id")
    @NotNull
    private Rol role;

    @Fetch(value = FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_categories",
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id"))
    private List<Category> categories;

    @Fetch(value = FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="user_sub_categories",
            joinColumns = @JoinColumn( name="user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn( name="sub_category_id", referencedColumnName = "id")
    )
    private List<SubCategory> subCategories;

    @Fetch(value = FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="user_schools"
    )
    private List<User> schools;

    @Fetch(value = FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="user_organizations"
    )
    private List<User> organizations;
    @NotNull
    private boolean agreeWithTerms;
    @NotNull
    private boolean deleted = false;

    @Column(name = "time_zone")
    private String timeZone; //0:00, +1:00, +2:00

    @Column(name = "facebook_id")
    private String facebookId;

    @Column(name = "google_id")
    private String googleId;

    @Column(name = "linkedin_id")
    private String linkedinId;

    @Column(name = "social_user")
    private Boolean socialUser;

    @Column(name = "email_confirmation_uid")
    @JsonIgnore
    private String emailConfirmationUID;

    @Column(name = "password_reset_token")
    @JsonIgnore
    private String passwordResetToken;

    @Column(name = "is_default_password_changed")
    private Boolean isDefaultPasswordChanged;


    @Column(name = "subscription_level")
    @Enumerated(EnumType.STRING)
    private SubscriptionPlanLevel subscriptionLevel;

    @Column(name = "subscription_org_type")
    @Enumerated(EnumType.STRING)
    private OrganizationType organizationType;

    @Column(name = "plan_active_until_date")
    private Date planActiveUntilDate;

    @Column(name = "stripe_id")
    private String stripeId;


    //private Date lastPaymentDate; //??
}
