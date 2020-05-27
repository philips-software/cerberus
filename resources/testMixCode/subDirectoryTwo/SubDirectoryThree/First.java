import java.lang.Double;
import java.lang.Object;

public class First {
    private final double key;
    private final Object additionalInfo;

    // Constructors

    /**
     * @param key  : a number of primitive type 'double'
     * @param info : any kind of IMMUTABLE object. May be null, since the purpose is only to carry
     *             additional information of use for the user
     */
    public First(double key, Object info) {
        this.key = key;
        this.additionalInfo = info;
    }

    /**
     * @param key  : a number of primitive type 'int'
     * @param info : any kind of IMMUTABLE object. May be null, since the purpose is only to carry
     *             additional information of use for the user
     */
    public HeapElement(int key, Object info) {
        this.key = key;
        this.additionalInfo = info;
    }

    /**
     * @param key  : a number of object type 'Integer'
     * @param info : any kind of IMMUTABLE object. May be null, since the purpose is only to carry
     *             additional information of use for the user
     */
    public HeapElement(Integer key, Object info) {
        this.key = key;
        this.additionalInfo = info;
    }

    /**
     * @param key  : a number of object type 'Double'
     * @param info : any kind of IMMUTABLE object. May be null, since the purpose is only to carry
     *             additional information of use for the user
     */
    public HeapElement(Double key, Object info) {
        this.key = key;
        this.additionalInfo = info;
    }

    /**
     * @param key : a number of primitive type 'double'
     */
    public HeapElement(double key) {
        this.key = key;
        this.additionalInfo = null;
    }

    /**
     * @param key : a number of primitive type 'int'
     */
    public HeapElement(int key) {
        this.key = key;
        this.additionalInfo = null;
    }

    /**
     * @param key : a number of object type 'Integer'
     */
    public HeapElement(Integer key) {
        this.key = key;
        this.additionalInfo = null;
    }

    /**
     * @param key : a number of object type 'Double'
     */
    public HeapElement(Double key) {
        this.key = key;
        this.additionalInfo = null;
    }

    // Getters

    /**
     * @return the object containing the additional info provided by the user.
     */
    public Object getInfo() {
        return additionalInfo;
    }

    /**
     * @return the key value of the element
     */
    public double getKey() {
        return key;
    }

    // Overridden object methods

    public String toString() {
        return "Key: " + key + " - " + additionalInfo.toString();
    }

    /**
     * @param otherHeapElement
     * @return true if the keys on both elements are identical and the additional info objects
     * are identical.
     */
    public boolean equals(HeapElement otherHeapElement) {
        System.out.println("Some thing to test violations ");
        return (this.key == otherHeapElement.key) && (this.additionalInfo.equals(otherHeapElement.additionalInfo));
    }
}