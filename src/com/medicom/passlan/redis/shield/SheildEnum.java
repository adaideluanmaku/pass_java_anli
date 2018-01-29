package com.medicom.passlan.redis.shield;

public enum SheildEnum {
	    /**< 剂量范围 */
	    PASS_DOSAGE(1,22),
	    /**< 肝损害剂量 */
        PASS_HEPDOSAGE(2,23),
        /**< 肾损害剂量 */
        PASS_RENDOSAGE(3,24), 
        /**< 相互作用 */
        PASS_DRUGINTER(4,25), 
        /**< 体外配伍 */
        PASS_IV(5,26), 
        /**< 配伍浓度 */
        PASS_DRUGLEVEL(6,27), 
        /**< 药物禁忌 */
        PASS_CONTRAIND(7,28),
        /**< 不良反应 */
        PASS_ADR(8,29), 
        /**< 超适应症 */
        PASS_INDICATION(9,30), 
        /**< 儿童用药 */
        PASS_PEDIATRIC(10,31),  
        /**< 成人用药 */
        PASS_ADULT(11,36), 
        /**< 老人用药 */
        PASS_GERIATRIC(12,32),
        /**< 妊娠用药 */
        PASS_PREGNANCY(13,33), 
        /**< 哺乳用药 */
        PASS_LACTATION(14,34), 
        /**< 性别用药 */
        PASS_SEX(15,37),   
        /**< 药物过敏 */
        PASS_DRUGALLERGEN(16,0), 
        /**< 给药途径 */
        PASS_ROUTE(17,0),   
        /**< 重复用药 */
        PASS_DUPLICATE(18,0),
        /**< 越权用药 */
        PASS_DOCPRIV(19,0),
        /**< 围术期用药 */
        PASS_PERIOPR(20,0),  
        /**< 细菌耐药率  */
        PASS_BACRESIS(21,0),  
        /**< 审查模块的个数 */
        NUM_SCREEN_MODULE(22,0);      
        
        // 成员变量
        private int	customindex;
        private int shieldindex;
        
        // 构造方法
        private SheildEnum(int customindex, int shieldindex) {
            this.customindex = customindex;
            this.shieldindex = shieldindex;
        }
        
        public  int getCustomindex() {
            return customindex;
        }

		public static  int getShieldindex(int customindex) {
			for (SheildEnum c : SheildEnum.values()) {
                if (c.getCustomindex() == customindex) {
                    return c.shieldindex;
                }
            }
            return 0;
		}
        
}
