package sg.nus.iss.shoppingcart.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.ArrayList;

@Entity
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long categoryId;

	@Column(nullable = false)
	private String name;

	private String description;

	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
	private List<Product> product = new ArrayList<>();

	public Category() {
	}

	public Category(String name, String description) {
		this.setName(name);
		this.setDescription(description);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return categoryId;
	}

	public void setId(Long id) {
		this.categoryId = categoryId;
	}

	@Override
	public String toString() {
		return "Product [id=" + categoryId + ", name=" + name + ", description=" + description + "]";
	}

}
