import java.io.*;
public class t2 {

    public  static class mapStruct{


        public linkedMapNode[] buckets;
        public int size = 0;


        public mapStruct(int z){
            buckets = new linkedMapNode[z];
        }

        public int getHash(String x){
            int h = 0;

            for(int i = 0; i < x.length(); i++)
                h = 31 * h + x.charAt(i);
            return Math.abs(h) % buckets.length;

        }

        public void putValue(String key, String value){

            int hash = getHash(key);
            linkedMapNode bucket = buckets[hash];


            if(bucket == null) {
                buckets[hash] = new linkedMapNode(key, value);
                size++;
                return;

            }

            while (true){


                if(bucket.key.equals(key)){
                    bucket.value = value;
                    break;
                }

                else if(bucket.next == null) {
                    bucket.next = new linkedMapNode(key, value);
                    bucket.next.prev = bucket;
                    size++;
                    break;
                }

                else {
                    bucket = bucket.next;
                }


            }
        }

        public String getValue(String key){
            if(size <= 0)
                return null;

            linkedMapNode bucket = buckets[getHash(key)];
            while (bucket != null){

                if(bucket.key.equals(key))
                    return bucket.value;
                bucket = bucket.next;

            }
            return null;
        }
        public void deleteValue(String key){
            if(size <= 0)
                return;
            int hash = getHash(key);
            linkedMapNode bucket = buckets[hash];

            while (bucket != null){
                if(bucket.key.equals(key)){

                    if(bucket.prev == null && bucket.next == null){
                        buckets[hash] = null;
                    }

                    else if(bucket.prev == null){
                        buckets[hash] = bucket.next;
                        bucket.next.prev = null;
                    }

                    else if(bucket.next == null){
                        bucket.prev.next = null;
                    }

                    else {
                        bucket.prev.next = bucket.next;
                        bucket.next.prev = bucket.prev;
                    }
                    size--;
                    return;
                }

                else {
                    bucket = bucket.next;
                }
            }
        }

    }
    public static class linkedMapNode {

        public linkedMapNode prev, next;
        public String key;
        public String value;

        public linkedMapNode(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }
    public static void main(String[] args)throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File("map.in")));
        mapStruct map = new mapStruct(1<<20);

        PrintWriter out = new PrintWriter("map.out");

        String line;

        while ((line = reader.readLine()) != null){

            String[] in = line.split(" ");
            String action = in[0];
            String key = in[1];

            if("put".equals(action)){
                map.putValue(key, in[2]);
            }

            else if("get".equals(action)){
                String value = map.getValue(key);
                out.println(value != null ? value : "none");
            }

            else if("delete".equals(action)){
                map.deleteValue(key);
            }
        }
        out.close();
        reader.close();
    }
}
