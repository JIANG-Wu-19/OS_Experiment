package Properties;

public class FreeBlock {//空闲块
    int index;
    int length;

    public FreeBlock(int index, int length) {
        this.index = index;
        this.length = length;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return "FreeBlock{" +
                "index=" + index +
                ", length=" + length +
                '}';
    }
}
