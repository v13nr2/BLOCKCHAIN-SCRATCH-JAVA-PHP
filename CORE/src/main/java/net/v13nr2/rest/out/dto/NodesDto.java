package net.v13nr2.rest.out.dto;

import java.util.StringJoiner;

public class NodesDto {


        private int id;
        private String name;
        private double price;
        private double weight;
        private String category;

        public NodesDto(int id, String name, double price, double weight,  String category) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.weight = weight;
            this.category = category;
        }

        public NodesDto(String name, double price, double weight,  String category) {
            this(0, name, price, weight,  category);
        }

        public NodesDto() {
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }


        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }


        @Override
        public String toString() {
            return new StringJoiner(", ", NodesDto.class.getSimpleName() + "[", "]")
                    .add("id=" + id)
                    .add("name='" + name + "'")
                    .add("price=" + price)
                    .add("weight=" + weight)
                    .add("category='" + category + "'")
                    .toString();
        }
    }
