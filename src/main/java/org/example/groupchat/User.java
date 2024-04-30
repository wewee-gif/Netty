package org.example.groupchat;

/**
 * @author:lmw
 * @date:2024/3/20 22:00
 **/
import java.io.*;

public class User implements Serializable {
    private String name;
    private transient int age; // transient 字段不会被序列化

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "User{name='" + name + "', age=" + age + "}";
    }

//    public static void main(String[] args) throws IOException, ClassNotFoundException {
//        User user = new User("Alice", 30);
//
//        // 序列化
//        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("user.ser"))) {
//            out.writeObject(user);
//        }
//
//        // 反序列化
//        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("user.ser"))) {
//            User deserializedUser = (User) in.readObject();
//            System.out.println(deserializedUser);
//        }
//    }
}
