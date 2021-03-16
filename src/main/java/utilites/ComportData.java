package utilites;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@Getter
public class ComportData {
    private final double currentPort1;
    private final double currentPort2;
    private final double currentPort3;
    private final double currentPort4;
    private final double tempPort1;
    private final double tempPort2;
    private final double tempPort3;
    private final double tempPort4;

    public ComportData(String[] input) {
        this.currentPort1 = Double.parseDouble(input[0]);
        this.currentPort2 = Double.parseDouble(input[1]);
        this.currentPort3 = Double.parseDouble(input[2]);
        this.currentPort4 = Double.parseDouble(input[3]);
        this.tempPort1 = Double.parseDouble(input[4]);
        this.tempPort2 = Double.parseDouble(input[5]);
        this.tempPort3 = Double.parseDouble(input[6]);
        this.tempPort4 = Double.parseDouble(input[7]);
    }
}
