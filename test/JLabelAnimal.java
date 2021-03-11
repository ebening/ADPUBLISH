import javax.swing.JLabel;
 
 
public class JLabelAnimal extends JLabel{
	private Animal animal;
 
	public JLabelAnimal(Animal a){
		this.setAnimal(a);
	}
 
	public void setAnimal(Animal animal) {
		this.animal = animal;
		this.setText(animal.getName() + " - " + animal.getAge());
	}
 
	public Animal getAnimal() {
		return animal;
	}
}