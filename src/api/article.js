import request from '@/utils/request.js'
import {useTokenStore} from '@/stores/token.js'
// 文章分类列表查询
export const articleCategoryListService = ()=>{
    // const tokenStore = useTokenStore();
    // tokenStore.token
    // 在pinia中定义的响应式数据 都不需要.value
    // return request.get('/category', {headers:{'Authorization':tokenStore.token}})
    return request.get('/category')
}

export const articleCategoryAddService = (categoryData)=>{
    return request.post('/category', categoryData)
}

// 文章分类修改
export const articleCategoryUpdateService = (categoryData)=>{
    return request.put('/category',categoryData)
}

// 文章分类删除