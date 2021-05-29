package sample;

import java.time.LocalDate;
import java.util.ArrayList;

public abstract class Person {
    private static int amount=0;
    private static int total=0;

    //Information about the person
    int ID;
    String name;
    String surname;
    LocalDate birthday;

    //Relationships
    ArrayList<Integer> Parents;
    ArrayList<Integer> Children;
    ArrayList<Integer> GrandChildren;
    ArrayList<Integer> GreatGrandChildren;
    int Spouse;
    ArrayList<Integer> Grandparents;
    ArrayList<Integer> GreatGrandparents;

    Person(String name, String surname, LocalDate birthday)
    {
        this.name = name;
        this.surname = surname;
        this.birthday = birthday;
        amount++;
        total++;
        this.ID = total;

        Spouse = 0;
        Parents = new ArrayList<>();
        Children = new ArrayList<>();
        GrandChildren = new ArrayList<>();
        GreatGrandChildren = new ArrayList<>();
        Grandparents = new ArrayList<>();
        GreatGrandparents = new ArrayList<>();
    }

    public int getAmount()
    {
       return amount;
    }

    static void setAmount(int amount1)
    {
        amount = amount1;
    }

    static void setTotal(int total1)
    {
        total = total1;
    }

    void reduceAmount()
    {
        amount--;
    }

    void addRelation(ArrayList<Integer> Relationship, int ID)
    {
        Relationship.add(ID);
    }

    void deleteRelation(int ID)
    {
        Parents.remove((Object)ID);
        Children.remove((Object)ID);
        Grandparents.remove((Object)ID);
        GreatGrandparents.remove((Object)ID);
        if(Spouse == ID) Spouse = 0;
    }

    abstract void update();

}
