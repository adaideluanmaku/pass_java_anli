package ch.com.sys;

public class Fenye {
	public int page=1;
	public int zpage=0;
//	public int num=0;
	
	public int getPage() {
		return page;
	}

	//输入数据总数，输入页面显示条数
	public int getZpage(int count,int sum) {
		if(count%sum>0){
			this.zpage=count/sum+1;
		}else{
			this.zpage=count/sum;
		}
		return zpage;
	}

//	public void Page(int zsnum){
//		
//		
//		if(page>zpage){
//			zsnum1=zsnum-((page-1)*10);
//			this.page=zpage;
//		}if(page<=zpage && page>=1){
//			zsnum1=zsnum-((page-1)*10);
//		}if(page<1){
//			this.page=1;
//			zsnum1=zsnum-((page-1)*10);
//		}
//		System.out.println("数据列表:"+zpage+":"+zsnum1);
//	}
}
