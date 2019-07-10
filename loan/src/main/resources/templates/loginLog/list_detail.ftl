<#list pageResult.content as log>
<tr>
	<td>${log.username}</td>
	<td>${log.loginTime}</td>
	<td>${log.ipAddress}</td>
	<td>${log.displayStatus}</td>
</tr>
</#list>

<script type="text/javascript">
	$(function(){
		$("#page_container").empty().append($('<ul id="pagination" class="pagination"></ul>'));
		$("#pagination").twbsPagination({
			totalPages:${pageResult.totalPages},
			startPage:${pageResult.page},
			first : "首页",
			prev : "上一页",
			next : "下一页",
			last : "最后一页",
			initiateStartPageClick:false,
			onPageClick : function(event, page) {
				$("#currentPage").val(page);
				$("#searchForm").submit();
			}
		});
	});
</script>
