function updateVal(id,val){
	if(id==null || typeof(id)=='undefined'){
		return;
	}
	var imgs=document.getElementsByTagName('img');
	if(imgs.length==0){
		return;
	}
	for(i=0;i<imgs.length;i++){
		var img=imgs[i];
		var group=img.getAttribute('group');
		if(group=='blank'){
			var vid=img.getAttribute('_id');
			if(vid==id){
				var parentNode=img.parentNode;
				var ppt=parentNode.getAttribute("tp");
				if(typeof(ppt)=='undefined' || ppt==null){
					var imgClone=img.cloneNode(true);
					imgClone.setAttribute('val',val);
					var newspan=document.createElement('span');
					newspan.setAttribute("tp","showVals");
					var subspan=document.createElement('span');
					subspan.setAttribute("tp","valspan");
					subspan.setAttribute('style','color:red;font-weight:bold;');
					subspan.innerHTML='('+val+')';
					newspan.appendChild(imgClone);
					newspan.appendChild(subspan);
					img.parentNode.replaceChild(newspan,img);
				}else if(ppt=='showVals'){
					var child=parentNode.childNodes;
					for(j=0;j<child.length;j++){
						var c=child[j];
						var subtp=c.getAttribute("tp");
						if(typeof(subtp)!='undefined' && subtp!=null){
							if(subtp=='valspan'){
								c.innerHTML=("("+val+")");
							}
						}
					}
				}
				break;
			}
		}
	}
}
function showBlank(s){
	if(s==null || typeof(s)=='undefined'){
		return;
	}
	var val=s.getAttribute('val');
	if(typeof(val)=='undefined' || val==null){
		val='';
	}
	tool.showBlank(s.getAttribute('tp'),s.getAttribute('_id'),val);
}