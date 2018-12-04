import java.io.*;

public class t3 {
    public static class linkedMapNode {
        public linkedMapNode insertedPrev, insertedNext;
        public linkedMapNode prev, next;
        public String key;
        public String value;

        public linkedMapNode(String key, String value) {

            this.key = key;
            this.value = value;

        }
    }

    public static class mapStruct{
        public linkedMapNode insertTail;

        public linkedMapNode[] buckets;
        public int size = 0;

        mapStruct(int z){
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
                attachInsertNode(buckets[hash]);
                size++;
                return;
            }

            while (true){

                if(bucket.key.equals(key)){
                    bucket.value = value;
                    break;
                }

                else if(bucket.next == null) {
                    linkedMapNode newNode = new linkedMapNode(key, value);
                    bucket.next = newNode;
                    bucket.next.prev = bucket;
                    attachInsertNode(newNode);
                    size++;
                    break;
                }

                else {
                    bucket = bucket.next;
                }

            }
        }

        private void attachInsertNode(linkedMapNode newNode) {

            if(insertTail != null)
                insertTail.insertedNext = newNode;

            newNode.insertedPrev = insertTail;
            insertTail = newNode;
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

        public linkedMapNode getNode(String key){

            if(size <= 0)
                return null;

            linkedMapNode bucket = buckets[getHash(key)];

            while (bucket != null){

                if(bucket.key.equals(key))
                    return bucket;

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

                    if(bucket.insertedPrev == null && bucket.insertedNext == null){
                        insertTail = null;
                    }

                    else if(bucket.insertedPrev == null){
                        bucket.insertedNext.insertedPrev = null;
                    }

                    else if(bucket.insertedNext == null){
                        bucket.insertedPrev.insertedNext = null;
                        insertTail = bucket.insertedPrev;
                    }

                    else {
                        bucket.insertedPrev.insertedNext = bucket.insertedNext;
                        bucket.insertedNext.insertedPrev = bucket.insertedPrev;
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



    public static void main(String []args)throws IOException{
        BufferedReader reader = new BufferedReader(new FileReader(new File("linkedmap.in")));
        mapStruct map = new mapStruct(1<<20);

        PrintWriter out = new PrintWriter("linkedmap.out");

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

            else if("prev".equals(action)){
                linkedMapNode node = map.getNode(key);
                if(node == null || node.insertedPrev == null){
                    out.println("none");
                }

                else {
                    out.println(node.insertedPrev.value);
                }
            }

            else if("next".equals(action)){
                linkedMapNode node = map.getNode(key);
                if(node == null || node.insertedNext == null){
                    out.println("none");
                }

                else {
                    out.println(node.insertedNext.value);
                }
            }
        }
        out.close();
        reader.close();
    }
}
