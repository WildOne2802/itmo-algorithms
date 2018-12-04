import java.io.*;

public class t4 {
    private static void printValue(PrintWriter out, SetStruct setStruct){
        if(setStruct == null){
            out.println(0);
            return;
        }
        out.print(setStruct.size + " ");
        for (int i = 0; i < setStruct.buckets.length; i++) {
            LinkedSetNode bucket = setStruct.buckets[i];
            while (bucket != null){
                out.print(bucket.value + " ");
                bucket = bucket.next;
            }
        }
		
        out.println();
    }

    public static int getStringHash(String x){
        int h = 0;
        for(int i = 0; i < x.length(); i++)
            h = 31 * h + x.charAt(i);
        return Math.abs(h);
    }

    public static class LinkedSetNode {

        public LinkedSetNode prev, next;
        public String value;

        public LinkedSetNode(String value) {
            this.value = value;
        }
    }

    public static class SetStruct {
        public LinkedSetNode[] buckets;
        public int size = 0;

        public SetStruct(int loadFactor){
            buckets = new LinkedSetNode[loadFactor];
        }

        public int getHash(String value){
            return getStringHash(value) % buckets.length;
        }

        public boolean contains(String value){
            if(size <= 0)
                return false;
            LinkedSetNode bucket = buckets[getHash(value)];
            while (bucket != null){
                if(bucket.value.equals(value))
                    return true;
                bucket = bucket.next;
            }
            return false;
        }

        public void insertValue(String value){
            if(contains(value))
                return;

            int hash = getHash(value);
            LinkedSetNode bucket = buckets[hash];
            if(bucket == null){
                buckets[hash] = new LinkedSetNode(value);
            } else {
                while (true){
                    if(bucket.next == null) {
                        bucket.next = new LinkedSetNode(value);
                        bucket.next.prev = bucket;
                        break;
                    } else {
                        bucket = bucket.next;
                    }
                }
            }
            size++;
        }

        public void deleteValue(String value){
            if(size <= 0)
                return;

            int hash = getHash(value);
            LinkedSetNode bucket = buckets[hash];
            while (bucket != null){
                if(bucket.value.equals(value)){
                    if(bucket.prev == null && bucket.next == null){
                        buckets[hash] = null;
                    } else if(bucket.prev == null){
                        buckets[hash] = bucket.next;
                        bucket.next.prev = null;
                    } else if(bucket.next == null){
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

    public static class linkedMapNode {

        public linkedMapNode prev, next;
        public String key;
        public SetStruct set;

        public linkedMapNode(String key) {
            this.key = key;
            this.set = new SetStruct(1 << 8);
        }
    }

    public static class multiMapStruct{
        public linkedMapNode[] buckets;
        public int size = 0;

        public multiMapStruct(int loadFactor){
            buckets = new linkedMapNode[loadFactor];
        }

        public int getHash(String x){
            return getStringHash(x) % buckets.length;
        }

        public void putValue(String key, String value){
            int hash = getHash(key);
            linkedMapNode bucket = buckets[hash];
            if(bucket == null) {
                buckets[hash] = new linkedMapNode(key);
                buckets[hash].set.insertValue(value);
                size++;
                return;
            }
            while (true){
                if(bucket.key.equals(key)){
                    bucket.set.insertValue(value);
                    break;
                } else if(bucket.next == null) {
                    bucket.next = new linkedMapNode(key);
                    bucket.next.set.insertValue(value);
                    bucket.next.prev = bucket;
                    size++;
                    break;
                } else {
                    bucket = bucket.next;
                }
            }
        }

        public SetStruct get(String key){
            if(size <= 0)
                return null;

            linkedMapNode bucket = buckets[getHash(key)];
            while (bucket != null){
                if(bucket.key.equals(key))
                    return bucket.set;
                bucket = bucket.next;
            }
            return null;
        }

        public void delete(String key){
            if(size <= 0)
                return;
            int hash = getHash(key);
            linkedMapNode bucket = buckets[hash];
            while (bucket != null){
                if(bucket.key.equals(key)){
                    if(bucket.prev == null && bucket.next == null){
                        buckets[hash] = null;
                    } else if(bucket.prev == null){
                        buckets[hash] = bucket.next;
                        bucket.next.prev = null;
                    } else if(bucket.next == null){
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

        public void deleteValue(String key, String value){
            if(size <= 0)
                return;
            int hash = getHash(key);
            linkedMapNode bucket = buckets[hash];
            while (bucket != null){
                if(bucket.key.equals(key)){
                    buckets[hash].set.deleteValue(value);
                    return;
                } else {
                    bucket = bucket.next;
                }
            }
        }

    }
    public static void main(String []args) throws IOException{
        BufferedReader reader = new BufferedReader(new FileReader(new File("multimap.in")));
        multiMapStruct multiMap = new multiMapStruct(1 << 20);
        PrintWriter out = new PrintWriter("multimap.out");
        String line;
        while ((line = reader.readLine()) != null){
            String[] in = line.split(" ");
            String action = in[0];
            String key = in[1];
            if("put".equals(action)){
                multiMap.putValue(key, in[2]);
            } else if("get".equals(action)){
                SetStruct set = multiMap.get(key);
                printValue(out, set);
            } else if("delete".equals(action)){
                multiMap.deleteValue(key, in[2]);
            } else if("deleteall".equals(action)){
                multiMap.delete(key);
            }
        }
        out.close();
        reader.close();
    }
}
