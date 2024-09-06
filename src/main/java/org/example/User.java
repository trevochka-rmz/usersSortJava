package org.example;

public class User {
        private String id;
        private String name;
        private String email;
        private int salary;

        public User(String id,String name, String email, int salary) {
            this.id =id;
            this.name = name;
            this.email = email;
            this.salary = salary;
        }
        public String getId() {
            return id;
        }
        public String getName() {
            return name;
        }
        public String getEmail() {
            return email;
        }

        public int getSalary() {
            return salary;
        }
}
