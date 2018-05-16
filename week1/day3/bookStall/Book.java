package bookStall;
import java.util.*;
import java.io.*;

public class Book{
	private String name;
	private Author[] authors;
	private Double price;
	private int qtyInStock = 0;

	Book(String name, Author[] authors, Double price, int qtyInStock){
		this.name = name;
		this.authors = authors;
		this.price = price;
		this.qtyInStock = qtyInStock;
	}
	Book(String name, Author authors, Double price, int qtyInStock){
		this.name = name;
		this.authors[0] = authors;
		this.price = price;
		this.qtyInStock = qtyInStock;
	}
	public String getName(){
		return this.name;
	}
	public Author[] getAuthors(){
		return this.authors;
	}
	public double getPrice(){
		return this.price;
	}
	public void setPrice(double price){
		this.price = price;
	}
	public int getQtyInStock(){
		return this.qtyInStock;
	}
	public void setQtyInStock(int qtyInStock){
		this.qtyInStock = qtyInStock;
	}
	public String toDisplay(){
		String display =  ("\n" + this.name + " by \n");
		for( int i = 0; i < this.authors.length; i++){
			display = display + this.authors[i].toDisplay() + "\n";
		}
		display = display + "Price: " + Double.toString(this.price) ;
		display = display + "\nNo of books available: " + Integer.toString(this.qtyInStock);
		return display;
	}
	public void printAuthors(){
		for( int i = 0; i < this.authors.length; i++){
			System.out.println(this.authors[i].toDisplay());
		}
	}
	public void addAuthor(Author author){
		Author[] arr = new Author[this.authors.length + 1];
		for(int i = 0; i<this.authors.length; i++){
			arr[i] = this.authors[i];
		}
		arr[this.authors.length] = author;
		this.authors = arr;
	}
	public static void main(String[] args){
		Scanner in = new Scanner(System.in);
		System.out.println("Enter name of the book ");
		String name = in.next();
		System.out.println("Enter no of authors ");
		int n = in.nextInt();
		Author[] auth = new Author[n] ;
		for(int i = 0; i<n; i++){
			System.out.println("Author " + (i+1));
			System.out.println("Name: ");
			String nameA = in.next();
			System.out.println("Email: ");
			String email = in.next();
			System.out.println("Gender: ");
			char gender = in.next().charAt(0);
			auth[i] = new Author(nameA, email, gender);
		}
		System.out.println("Enter price of the book ");
		Double price = in.nextDouble();
		System.out.println("Enter quantity of books available ");
		int qtyInStock = in.nextInt();
		Book book = new Book(name, auth, price, qtyInStock);
		System.out.println("Add author to " + book.getName() + ": ");

		System.out.println("Name: ");
		String nameA = in.next();
		System.out.println("Email: ");
		String email = in.next();
		System.out.println("Gender: ");
		char gender = in.next().charAt(0);
		Author authToAdd = new Author(nameA, email, gender);
		book.addAuthor(authToAdd);
		book.printAuthors();
		System.out.println(book.toDisplay());
	}
}