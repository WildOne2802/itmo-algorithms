import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
 
public class t1 {
    public static void main(String [] args) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("set.in"));
        setStruct set = new setStruct(1<<20);
 
 
        PrintWriter out = new PrintWriter("set.out");
        String line;
        while ((line = reader.readLine()) != null){
            String[] in = line.split(" ");
            String action = in[0];
            int value = Integer.parseInt(in[1]);
            if("insert".equals(action)){
                set.insert(value);
            } else if("exists".equals(action)){
                out.println(set.contains(value));
            } else if("delete".equals(action)){
                set.delete(value);
            }
        }
        out.close();
        reader.close();
    }
    public static class linkedSetNode {
 
        public linkedSetNode prev, next;
        public int value;
 
        public linkedSetNode(int value) {
            this.value = value;
        }
    }
    public static class setStruct {
        public linkedSetNode[] buckets;
        public int size = 0;
 
        setStruct(int z) {
            buckets = new linkedSetNode[z];
        }
 
        public int getHash(int x) {
            x = ((x >> 16) ^ x) * 0x45d9f3b;
            x = ((x >> 16) ^ x) * 0x45d9f3b;
            x = (x >> 16) ^ x;
            return Math.abs(x) % buckets.length;
        }
        public boolean contains(int value){
            if(size<=0){
                return false;
            }
            linkedSetNode bucket=buckets[getHash(value)];
            while(bucket!=null){
                if(bucket.value==value)
                    return true;
                bucket = bucket.next;
            }
            return false;
        }
        public void insert(int value){
            if(contains(value)){
                return;
            }
            int hash =getHash(value);
            linkedSetNode bucket = buckets[hash];
 
            if(bucket==null){
                buckets[hash]= new linkedSetNode(value);
 
            }else{
                while(true){
                    if(bucket.next==null){
                        bucket.next = new linkedSetNode(value);
                        bucket.next.prev = bucket;
                        break;
                    }
                    else{
                        bucket=bucket.next;
                    }
                }
            }
            size++;
        }
        public void delete(int value) {
            if (size <= 0)
                return;
 
            int hash = getHash(value);
            linkedSetNode bucket = buckets[hash];
            while (bucket != null) {
                if (bucket.value == value) {
                    if (bucket.prev == null && bucket.next == null) {
                        buckets[hash] = null;
                    } else if (bucket.prev == null) {
                        buckets[hash] = bucket.next;
                        bucket.next.prev = null;
                    } else if (bucket.next == null) {
                        bucket.prev.next = null;
                    } else {
                        bucket.prev.next = bucket.next;
                        bucket.next.prev = bucket.prev;
                    }
                    size--;
                    return;
                } else {
                    bucket = bucket.next;
                }
            }
        }
    }
}