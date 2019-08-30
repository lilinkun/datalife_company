package aicare.net.cn.iweightlibrary.entity;

public class User {
    private int id;//id为0x7F是为游客
    private int sex;//性别 1:男; 2:女
    private int age;//年龄
    private int height;//身高
    private int weight;//体重
    private int adc;//阻抗值

    public User() {
    }

    public User(int id, int sex, int age, int height, int weight, int adc) {
        this.id = id;
        this.sex = sex;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.adc = adc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getAdc() {
        return adc;
    }

    public void setAdc(int adc) {
        this.adc = adc;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", sex=" + sex +
                ", age=" + age +
                ", height=" + height +
                ", weight=" + weight +
                ", adc=" + adc +
                '}';
    }
}
