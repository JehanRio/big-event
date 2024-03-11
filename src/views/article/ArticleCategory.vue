<script setup>
import {
    Edit,
    Delete
} from '@element-plus/icons-vue'
import { ref } from 'vue'
const categorys = ref([
    // {
    //     "id": 3,
    //     "categoryName": "美食",
    //     "categoryAlias": "my",
    //     "createTime": "2023-09-02 12:06:59",
    //     "updateTime": "2023-09-02 12:06:59"
    // },
    // {
    //     "id": 4,
    //     "categoryName": "娱乐",
    //     "categoryAlias": "yl",
    //     "createTime": "2023-09-02 12:08:16",
    //     "updateTime": "2023-09-02 12:08:16"
    // },
    // {
    //     "id": 5,
    //     "categoryName": "军事",
    //     "categoryAlias": "js",
    //     "createTime": "2023-09-02 12:08:33",
    //     "updateTime": "2023-09-02 12:08:33"
    // }
])
// 声明一个异步的函数
import {articleCategoryListService, articleCategoryAddService,articleCategoryUpdateService} from '@/api/article.js'
const articleCategoryList = async()=>{
    let res = await articleCategoryListService();
    categorys.value = res.data;
}

//控制添加分类弹窗
const dialogVisible = ref(false)

//添加分类数据模型
const categoryModel = ref({
    categoryName: '',
    categoryAlias: ''
})
//添加分类表单校验
const rules = {
    categoryName: [
        { required: true, message: '请输入分类名称', trigger: 'blur' },
    ],
    categoryAlias: [
        { required: true, message: '请输入分类别名', trigger: 'blur' },
    ]
}



// 调用接口，添加表单
import {ElMessage} from 'element-plus'
const addCategory = async()=>{
    // 调用接口
    let res = await articleCategoryAddService(categoryModel.value);
    ElMessage.success(res.msg ? res.msg : '添加成功')
    // 调用获取所有文章的函数
    articleCategoryList();
    dialogVisible.value = false;
}

// 编辑分类
const updateCategory = async()=>{
    // 调用接口
    let res = await articleCategoryUpdateService(categoryModel.value);
    ElMessage.success(res.msg ? res.msg : '修改成功')
    // 调用获取所有文章的函数
    articleCategoryList();
    // 隐藏弹窗
    dialogVisible.value = false;
}

const title = ref('')
articleCategoryList();

// 展示编辑弹窗
const showDialog = (row)=>{
    dialogVisible.value = true;
    title.value = '编辑分类';
    // 数据拷贝
    categoryModel.value.categoryName = row.categoryName;
    categoryModel.value.categoryAlias = row.categoryAlias;
    // 扩展id属性，将来需要传递给后台 完成分类的修改
    categoryModel.value.id = row.id;
}

// 删除分类
const deleteCategory = ()=>{

}

</script>
<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>文章分类</span>
                <div class="extra">
                    <el-button type="primary" @click="dialogVisible=true;title='添加分类'">添加分类</el-button>
                </div>
            </div>
        </template>
        <el-table :data="categorys" style="width: 100%">
            <el-table-column label="序号" width="100" type="index"> </el-table-column>
            <el-table-column label="分类名称" prop="categoryName"></el-table-column>
            <el-table-column label="分类别名" prop="categoryAlias"></el-table-column>
            <el-table-column label="操作" width="100">
                <template #default="{ row }">
                    <el-button :icon="Edit" circle plain type="primary" @click="showDialog(row)"></el-button>
                    <el-button :icon="Delete" circle plain type="danger" @click="deleteCategory"></el-button>
                </template>
            </el-table-column>
            <template #empty>
                <el-empty description="没有数据" />
            </template>
        </el-table>

        <!-- 添加分类弹窗 当v-model为true时才显示-->
        <el-dialog v-model="dialogVisible" :title="title" width="30%">    
            <el-form :model="categoryModel" :rules="rules" label-width="100px" style="padding-right: 30px">
                <el-form-item label="分类名称" prop="categoryName">
                    <el-input v-model="categoryModel.categoryName" minlength="1" maxlength="10"></el-input>
                </el-form-item>
                <el-form-item label="分类别名" prop="categoryAlias">
                    <el-input v-model="categoryModel.categoryAlias" minlength="1" maxlength="15"></el-input>
                </el-form-item>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="dialogVisible = false">取消</el-button>
                    <el-button type="primary" @click="title=='添加分类'?addCategory():updateCategory()"> 确认 </el-button>
                </span>
            </template>
        </el-dialog>

    </el-card>
</template>

<style lang="scss" scoped>
.page-container {
    min-height: 100%;
    box-sizing: border-box;

    .header {
        display: flex;
        align-items: center;
        justify-content: space-between;
    }
}
</style>