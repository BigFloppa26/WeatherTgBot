package ustin.models;

public class Person {
    private double num;
    private String firstName;
    private String lastName;
    private String country;
    private String gender;
    private double age;
    private String date;
    private double id;

    public Person() {}

    public Person(double num, String firstName, String lastName, String country, String gender, double age, String date, double id) {
        this.num = num;
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
        this.gender = gender;
        this.age = age;
        this.date = date;
        this.id = id;
    }

    @Override
    public String toString() {
        return "Person{" +
                "num=" + num +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", country='" + country + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", date=" + date +
                ", id=" + id +
                '}';
    }

    public double getNum() {
        return num;
    }

    public void setNum(double num) {
        this.num = num;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public double getAge() {
        return age;
    }

    public void setAge(double age) {
        this.age = age;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }
}
