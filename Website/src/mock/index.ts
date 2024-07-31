import Mock from 'mockjs'

{
    url: "data/list";
    method: "post";
    response: (data: { body: string; }) => {
        console.log(data);
        console.log(JSON.parse(data.body));
        return {
            status: 200,
            message: 'success',
            data: {
                'name': '@cname', // 中文名称
            }
        }
    }
}
