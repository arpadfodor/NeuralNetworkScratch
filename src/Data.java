import java.util.ArrayList;
import java.util.List;

//contains the input data given by the user and the output data generated by the network
public class Data {

	//teacher-, validation-, test input lists
	private ArrayList<ArrayList<Double>> teacher;
	private ArrayList<ArrayList<Double>> validation;
	private ArrayList<ArrayList<Double>> test;
	//result output list
	private ArrayList<ArrayList<Double>> result = new ArrayList();

	//lists to store the edge values to normalize
    private List<Double> teacher_min;
    private List<Double> teacher_max;
    private List<Double> validation_min;
    private List<Double> validation_max;
	
	public Data(ArrayList<ArrayList<Double>> teacher_input, ArrayList<ArrayList<Double>> validation_input, ArrayList<ArrayList<Double>> test_input) {
		this.teacher = teacher_input;
		this.validation = validation_input;
		this.test = test_input;
	}
	
	//normalizes the data of the class
	public void normalize() {

		teacher_min = new ArrayList(teacher.get(0));
		teacher_max = new ArrayList(teacher.get(0));
		validation_min = new ArrayList(validation.get(0));
		validation_max = new ArrayList(validation.get(0));
		
		for(int i = 0; i < teacher.size(); i++) {
			
			List<Double> teacher_data = new ArrayList(teacher.get(i));
			List<Double> validation_data = new ArrayList(validation.get(i));
			
			for(int j = 0; j < teacher_data.size(); j++) {
				
				if(teacher_max.get(j) < teacher_data.get(j)) {
					teacher_max.set(j, teacher_data.get(j));
				}
				
				if(teacher_min.get(j) > teacher_data.get(j)) {
					teacher_min.set(j, teacher_data.get(j));
				}
				
			}
			
			for(int j = 0; j < validation_data.size(); j++) {
				
				if(validation_max.get(j) < validation_data.get(j)) {
					validation_max.set(j, validation_data.get(j));
				}
				
				if(validation_min.get(j) > validation_data.get(j)) {
					validation_min.set(j, validation_data.get(j));
				}
				
			}
			
		}
		
		NormalizeData(teacher, teacher_min, teacher_max);
		NormalizeData(validation, validation_min, validation_max);
		NormalizeData(test, teacher_min, teacher_max);
        
	}
	
	//normalization of the given data list with the given edge values
    private void NormalizeData(ArrayList<ArrayList<Double>> source, List<Double> min, List<Double> max) {
    	
        for (int i = 0; i < source.size(); i++) {
        	
            ArrayList<Double> data = new ArrayList<Double>(source.get(i));
            
            for (int j = 0; j < data.size(); j++) {
            	
            	//if the range is real, otherwise avoid zero division
            	if(!min.get(j).equals(max.get(j))) {
            		
            		Double temp = data.get(j);
                	temp = (temp - min.get(j)) / (max.get(j) - min.get(j));
                    data.set(j, temp);
            		
            	}
                
            }
            
            source.set(i, data);
            
        }
        
    }
    
	//transfers the result to be meaningful data
	public void denormalize() {

		for (ArrayList<Double> doubles : result) {

			for (int j = 0; j < doubles.size(); j++) {

				//if the range is real, otherwise avoid zero division
				if (!validation_max.get(j).equals(validation_min.get(j))) {
					double actual = doubles.get(j);
					actual = (actual * (validation_max.get(j) - validation_min.get(j))) + validation_min.get(j);
					doubles.set(j, actual);
				}

			}

		}
		
	}
	
	public ArrayList<ArrayList<Double>> GetTeacher(){
		return this.teacher;
	}
	
	public ArrayList<ArrayList<Double>> GetValidation(){
		return this.validation;
	}
	
	public ArrayList<ArrayList<Double>> GetTest(){
		return this.test;
	}
	
	public ArrayList<ArrayList<Double>> GetResult(){
		return this.result;
	}
	
	public void writeResult() {

		System.out.print("Predicted result: ");

		for (ArrayList<Double> row : result) {
			for (Double element : row) {
				System.out.print(element + " ");
			}
			System.out.println();
		}
		
	}
	
}