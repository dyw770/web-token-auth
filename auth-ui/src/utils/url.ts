/**
 * @param {...string} paths - 要拼接的路径参数
 * @returns {string} 拼接后的完整路径
 */
export function joinPaths(...paths: string[]): string {
    if (!paths || paths.length === 0) {
        return ''
    }
    let result = paths[0]
    // 处理剩余的路径
    for (let i = 1; i < paths.length; i++) {
        const path = paths[i]
        if (!path || path.length === 0) {
            continue
        }

        // 检查result的最后一个字符是否为斜杠
        const resultEndsWithSlash = result[result.length - 1] === '/'
        // 检查当前path的第一个字符是否为斜杠
        const pathStartsWithSlash = path[0] === '/'

        if (resultEndsWithSlash && pathStartsWithSlash) {
            // 如果result以斜杠结尾且path以斜杠开头，只保留一个斜杠
            result += path.substring(1)
        } else if (!resultEndsWithSlash && !pathStartsWithSlash) {
            // 如果result不以斜杠结尾且path不以斜杠开头，添加一个斜杠
            result += '/' + path
        } else {
            // 其他情况，直接拼接
            result += path
        }
    }

    return result
}

export default {
    joinPaths
}