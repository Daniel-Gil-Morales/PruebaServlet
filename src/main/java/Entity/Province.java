package Entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Province {

    private int id;

    private String code;

    private String name;


    public Province(String code, String name) {
        this.code = code;
        this.name = name;

    }
}