package com.example.hp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FaceFutureInfoPack {

    /**
     * status : 0
     * data : [{"userId":1004,"userName":"刘沛栋","avatarUrl":"http://www.shidongxuan.top/image/28d5841b-84ef-4a17-8467-f825eedd9307.jpg","faceData":"AAD6RAAAdEOONnO95Qd6vHAKmz2sBkM705VuPQviTjxiXKs8uiqTvUn+5jyOYcC9b6wTvM0+3r0B\nlgi+7JxuvNHpujzI00m9JxCiPYyEkLwelDs+XyyyvfBiuD2/e0u9Ike3vKdp2r0455C9M/bxuyPc\n77tVCAo+7c83vV8Thr2Rjvw8dySuPSsFb73cOqe8oXIBvZbWrT1sZos7kWxuPF8aor3vxSm9lAjM\nPFAijzxB/lm913UjvgEgyD1CWsQ9uswlPoM1Rr09wc+89Wd2PGsuAT3lQSI8tyMMvY2QJjp6vAk9\nH69vveZRkD1ZDqg7r5s8vbj9FTzRrVE9Wv+KvRgyIj08QmQ9mr1vvTXOfbxf2CM9acgpPaV2pDt2\nsDO9cn8Mvp/Akb2eXjo90wIXvZA5kz17cRa97LJ2vAx0AD1U4zm82IwFPeWAeb34vou9J+7SPSH0\naD16uIE9ATTkPaytNT1ZOty9oxqIvbR/er2Z3Ss9cOo5PW+X4j1viPw5f9sivaxmCT2JTxG+JBwk\nvmintj3KPfu9zwv8u1zH7b0+5Z69ISGjvPhktL3stlE9Nro0PEp/3Lwj/dC7ztYWvpxQbL3cXyY9\nezGFvVBAST2jvWW9Sm/NPPS4uD08xaE8Hw0tvSknrLvDk3s9zh+9vZjMsLzGP5q81Wx6vXV4kbsA\npzU9OcVsvVlZaT1j/q29N7mUPW/eGj1dp/g8EzM3PStNTr3OXi69mIstPQF9Fj0v2MG9ffqdvLFp\nyzyJkY89s5FDvacYOL2H6lG9UJzSPOGSfLpCvV+8XUIovRChir3CK4s9Ii5tvUH4CD3f72S76VmC\nPdV0PztBQXs8hYymvRqe8jw83wC8cL2QPUUhkL1bUzG9jGjwvAw/Tz2++wW8ZCK4PQckgD2Dj1K8\nJ9DWPX+HOb1Isgq83cIFvaUyiz1dvb6836IAPRAR+r0DqTO8wLv7vLPQAj7m6IO4AW3ePdVvRz2w\nl3W9owGjvVOZHD2DhxQ8D5NLPUUhwzwBLk49U49IPUryCbwMYI09dyC/vR+NDD1TzQa8b6ievYfp\nbj0cVjg9byd4vSI+y7xOqRk9krpMvRyDpTwUJ7A8KvGrvaDt3DyY/LE9S2GbPTjDuztQSks9BRGl\nPd+Y97xINMU9PqhSPO/wDz2ZDIU9dHD8PTW/Sb1VY6i9AL2jPLM38bwzmFu9m9OYvXN0t72wgNM8\nx8lVvXXMI71aYY69GguXPAiRoLxVXWy96l+JvUkPgzzo+CU97TTcPeJOPr1wvYa9bzZYvMg6vbxT\nMrW75KpQPEBS8Dyxk9c8RsunPZARtT2MQsA9cfgJPqheY71RDu08zXGHvQM/iL1uYxo9G2N4PBeJ\nAL085W+9"},{"userId":1009,"userName":"辣鸡","avatarUrl":"http://www.shidongxuan.top/image/c5f2f949-adf5-4a1c-86c1-0474064176c4.jpg","faceData":"AAD6RAAAdEPdw6690uC+PNVy7zw5GeI8f2XwvLiM27xkp0C9sc/2ObB5Vb0v7rG9pol2ve8Vwb2/\nI+S9y2GHuEI20zrfyD09LaEGPUo0AbxgY8M9UEWRvRXiPDxIwUu8yugCvc9un7xKHNs8iXVzvdXU\nLr2m4Wo8OmL4vBkMRDzNaNo8qoKjOkRrbb3Cb8Y8jViIvZzSnTzzSA89FI41Pv35Db6AeP69PeaB\nPDO0lL0hcfe8Piy2vTIAUj380qk83+A8PbE6lD0nxJ+7Dc5ovRVYwzxG5Qo9RmjmPd09oTza6Is8\nb7MCvebayr1lb5C8zNAVvHKsAz7LCAe98YIAPgbQgTwnkhA+wie5PB1jaTxk9aq9w8UAvrjoFz0Y\n1Ai9J0M2vZC/iL2AkwC9u2vJvYkOXTzZz4s9e7FDPBMUiT0pxKA9DQkyvMnSAj2Ajuo8pdFZOqG5\nkj2jsmk8a2U0PQaqvL3Skce6KiCAvVbs1z3U8Ps7BksqPXgPfj35icG9tF8nPTHvsj15RAK+exPF\nvcbSyzwAJYG9k4jAOyDIPL3x1XG83mgBvUKBrz1FfKg9cGqXvecuGzztXps8LkkYvZ+MJz1V9Bm8\n4mZ2vTAxFz0BqAW+cEWbvcJl1zxHSYe7Ht3QvHOU7TsV/ss8j5zrvCko6zrD0JA6UmdDvCWeBj0Z\nQkG9iaQBPkP7tru1IGa8sYvvPASxML0zyRA9E4YWvTp8JD0jzlc87qycPTZbyT1wc+i9AnvAPCL7\nvr2jzYs9M99zvazyr71qop69xsAkPEFWpT34Z7S8mbQ6vkTBcr3Xj5O9Vk75vPQqZj3fwQ++CEyD\nvaXP1Ty+KpG9qPBavKKVPr1awuC8DC9YPSsvt7wUZZm92RsNPOVsyD0R1kS8IiyOPBI9cj1gPXG9\n2PQVPaZIvb24mT68nBIrvXP+lD2lyYw8BFpuvQspIb2Qdrs9Y+0UvTQAYjwufHG74DHwPYIvvL31\nzwQ9eoy0vYlS1z0M1y674OsAPpdEwj3G6n49c4r1vGsinz2CIQy9jgaxPPo1X70gmro8QxDVvKz9\nE70JIUi9tukVvJ0G9r1Vho49ipMJPaPjZDzXgkY8cKf7PEfgFb2Hqmm91+sRPSRDNj1mLQc+B7ew\nPbnWMj0hIZc9J8OqPQsfID7ddZK81ltdvXxqjLzqcsy7HUA7PRkPA71AJ9O8xSnqvYUN972rKG49\n7DE9vK0sn73znmU9n3X8POemfb1diL28vq3cO9ztG7zF5/w8NGA3PbHXnzs120C8bTKhPAuK37w7\n7NE9vR36PGPNWT1oLru8nUD9O50cRb2dLIW9lP1TPfA07byNAIs8CAzHPYD1rj0ET0494CswvHHn\n170kYQC+"}]
     */

    private int status;

    @SerializedName("data")
    private List<FaceFutureInfo> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<FaceFutureInfo> getData() {
        return data;
    }

    public void setData(List<FaceFutureInfo> data) {
        this.data = data;
    }

    public static class FaceFutureInfo {
        /**
         * userId : 1004
         * userName : 刘沛栋
         * avatarUrl : http://www.shidongxuan.top/image/28d5841b-84ef-4a17-8467-f825eedd9307.jpg
         * faceData : AAD6RAAAdEOONnO95Qd6vHAKmz2sBkM705VuPQviTjxiXKs8uiqTvUn+5jyOYcC9b6wTvM0+3r0B
         lgi+7JxuvNHpujzI00m9JxCiPYyEkLwelDs+XyyyvfBiuD2/e0u9Ike3vKdp2r0455C9M/bxuyPc
         77tVCAo+7c83vV8Thr2Rjvw8dySuPSsFb73cOqe8oXIBvZbWrT1sZos7kWxuPF8aor3vxSm9lAjM
         PFAijzxB/lm913UjvgEgyD1CWsQ9uswlPoM1Rr09wc+89Wd2PGsuAT3lQSI8tyMMvY2QJjp6vAk9
         H69vveZRkD1ZDqg7r5s8vbj9FTzRrVE9Wv+KvRgyIj08QmQ9mr1vvTXOfbxf2CM9acgpPaV2pDt2
         sDO9cn8Mvp/Akb2eXjo90wIXvZA5kz17cRa97LJ2vAx0AD1U4zm82IwFPeWAeb34vou9J+7SPSH0
         aD16uIE9ATTkPaytNT1ZOty9oxqIvbR/er2Z3Ss9cOo5PW+X4j1viPw5f9sivaxmCT2JTxG+JBwk
         vmintj3KPfu9zwv8u1zH7b0+5Z69ISGjvPhktL3stlE9Nro0PEp/3Lwj/dC7ztYWvpxQbL3cXyY9
         ezGFvVBAST2jvWW9Sm/NPPS4uD08xaE8Hw0tvSknrLvDk3s9zh+9vZjMsLzGP5q81Wx6vXV4kbsA
         pzU9OcVsvVlZaT1j/q29N7mUPW/eGj1dp/g8EzM3PStNTr3OXi69mIstPQF9Fj0v2MG9ffqdvLFp
         yzyJkY89s5FDvacYOL2H6lG9UJzSPOGSfLpCvV+8XUIovRChir3CK4s9Ii5tvUH4CD3f72S76VmC
         PdV0PztBQXs8hYymvRqe8jw83wC8cL2QPUUhkL1bUzG9jGjwvAw/Tz2++wW8ZCK4PQckgD2Dj1K8
         J9DWPX+HOb1Isgq83cIFvaUyiz1dvb6836IAPRAR+r0DqTO8wLv7vLPQAj7m6IO4AW3ePdVvRz2w
         l3W9owGjvVOZHD2DhxQ8D5NLPUUhwzwBLk49U49IPUryCbwMYI09dyC/vR+NDD1TzQa8b6ievYfp
         bj0cVjg9byd4vSI+y7xOqRk9krpMvRyDpTwUJ7A8KvGrvaDt3DyY/LE9S2GbPTjDuztQSks9BRGl
         Pd+Y97xINMU9PqhSPO/wDz2ZDIU9dHD8PTW/Sb1VY6i9AL2jPLM38bwzmFu9m9OYvXN0t72wgNM8
         x8lVvXXMI71aYY69GguXPAiRoLxVXWy96l+JvUkPgzzo+CU97TTcPeJOPr1wvYa9bzZYvMg6vbxT
         MrW75KpQPEBS8Dyxk9c8RsunPZARtT2MQsA9cfgJPqheY71RDu08zXGHvQM/iL1uYxo9G2N4PBeJ
         AL085W+9
         */

        private int userId;
        private String userName;
        private String avatarUrl;
        private String faceData;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public String getFaceData() {
            return faceData;
        }

        public void setFaceData(String faceData) {
            this.faceData = faceData;
        }
    }
}
