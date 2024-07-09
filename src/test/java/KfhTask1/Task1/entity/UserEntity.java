package KfhTask1.Task1.entity;


import javax.persistence.*;

@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;



    @Column(nullable = false)
    private String password;
    @Column(name = "username", nullable = false)
    private String username;

    public String getName() {
        return name;
    }
    @OneToOne
    @JoinColumn(name = "role_id")
    private RoleEntity role;
    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
