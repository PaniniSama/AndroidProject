package ensa.application01.androidproject_ginf3;

public class User {

    //Classe de l'utilisateur, nous l'utiliserons pour avoir des objets user qu'on va faire passer à firebase
    public String fullname,age,email;

    public User(){} // constructeur par defaut qui sera vide pour pouvoir acceder aux informations directemenr dans l'activié profile

    public User(String fullname,String age,String email){
        this.fullname=fullname;
        this.age=age;
        this.email=email;
    }

}
