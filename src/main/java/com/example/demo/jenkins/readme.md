清理构建脚本：
在脚本执行框中执行以下命令：
def jobName = "health-assistant-test"

def maxNumber = 540

Jenkins.instance.getItemByFullName(jobName).builds.findAll {

it.number <= maxNumber

}.each {

it.delete()

}